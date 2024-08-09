## Cajullenge

#### L4. Questão aberta

O cenário descrito demonstra um desafio recorrente na implementação de sistemas distribuídos com o uso de banco de dados
que é o anti-pattern ***read-process-write***.

A sequência abaixo ilustra isso:

| Transação 1                                                                                       |                                            Transação 2                                            |
|:--------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------:|
| SELECT balance FROM category_balance WHERE account_id = 1 AND category_id = 'FOOD'; (retorna 500) |                                                                                                   |
|                                                                                                   | SELECT balance FROM category_balance WHERE account_id = 1 AND category_id = 'FOOD'; (retorna 500) |
| Executa alguma lógica para executar o débito no valor de 200 (500 – 200 = 300)                    |                                                                                                   |
|                                                                                                   |    Executa alguma lógica para executar o débito no valor de 200 (500 – 200 = 300)                 |
| UPDATE category_balance SET balance = 300 WHERE account_id = 1 AND category_id = 'FOOD';          |                                                                                                   |
|                                                                                                   |     UPDATE category_balance SET balance = 300 WHERE account_id = 1 AND category_id = 'FOOD';      |

Ao término das duas transações o saldo estará com o valor incosistente de 300 quando o correto seria 100, 
pois o saldo inicial era 500 e ocorreram dois débitos no valor de 200.

Como o banco de dados escolhido para a implementação do desafio foi o PostgreSQL, 
para evitarmos esse tipo de problema podemos lançar mão do ***Row-Level Locks*** 
que uma estratégia de ***pessimistic locking***.
Conseguimos isso usando o modo ***FOR UPDATE***. 
Na prática fazemos uma alteração na query de SELECT adicionando ao final o FOR UPDATE

Com o uso do ***FOR UPDATE*** conseguimos com que as linhas recuperadas pela query de SELECT 
sejam bloqueadas até que seja confirmada uma atualização. 
Isso impede que elas sejam bloqueadas, modificadas ou excluídas por outras transações até que a transação atual seja concluída.
Ou seja, outras transações que tentarem fazer alguma atualização ou um ***SELECT FOR UPDATE*** da linha que está bloqueada deverão aguardar até que a transação atual termine.

A sequência abaixo ilustra o comportamento quando usamos o FOR UPDATE:

| Transação 1                                                                                                  |                                           Transação 2                                           |
|:-------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------:|
| SELECT balance FROM category_balance WHERE account_id = 1 AND category_id = 'FOOD' FOR UPDATE; (retorna 500) |                                                                                                 |
|                                                                                                              | SELECT balance FROM category_balance WHERE account_id = 1 AND category_id = 'FOOD' FOR UPDATE;  |
| Executa alguma lógica para executar o débito no valor de 200 (500 – 200 = 300)                               |                                                                                                 |
| UPDATE category_balance SET balance = 300 WHERE account_id = 1 AND category_id = 'FOOD';                     |                                                                                                 |
| COMMIT;                                                                                                      |                                                                                                 |
|                                                                                                              | O SELECT FOR UPDATE da Transação 2 só recebe retorno após o COMMIT da Transação 1 (retorna 300) |
|                                                                                                              |         Executa alguma lógica para executar o débito no valor de 200 (300 – 200 = 100)          |
|                                                                                                              |    UPDATE category_balance SET balance = 100 WHERE account_id = 1 AND category_id = 'FOOD';     |
|                                                                                                              |                                             COMMIT;                                             |

#### Para rodar na sua máquina os requisitos são:

* Java 21
* Docker

#### Base de dados

Existe um arquivo docker-compose.yaml que quando executado provisiona uma base de dados e executa uma migration 
via flyway construíndo tabelas e inserindo dados iniciais.

#### Testes

Foram implementados testes unitários e testes E2E. Durante a execução dos testes E2E é provisionada uma base de dados 
via testcontainers. São inseridos alguns dados específicos para os testes e esses são inseridos via migration flyway. 
Os testes E2E validam os principais requisitos do desafio.