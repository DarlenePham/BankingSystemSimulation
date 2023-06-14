--
-- db2 -td"@" -f p2.sql
--
CONNECT TO CS157A@
--
--
DROP PROCEDURE P2.CUST_CRT@
DROP PROCEDURE P2.CUST_LOGIN@
DROP PROCEDURE P2.ACCT_OPN@
DROP PROCEDURE P2.ACCT_CLS@
DROP PROCEDURE P2.ACCT_DEP@
DROP PROCEDURE P2.ACCT_WTH@
DROP PROCEDURE P2.ACCT_TRX@
DROP PROCEDURE P2.ADD_INTEREST@
DROP PROCEDURE P2.LOGIN_CHECK@
DROP PROCEDURE P2.GET_ID@
DROP PROCEDURE P2.ACC_NUM@
DROP PROCEDURE P2.CUST_VALID@
DROP PROCEDURE P2.ACC_STS@
DROP PROCEDURE P2.MONEY_CHECK@
--
--
CREATE PROCEDURE P2.GET_ID
(OUT id INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for select max(ID) from p2.customer;
    
    open c1;
    fetch c1 into id;
    close c1;
END @
--
CREATE PROCEDURE P2.CUST_CRT
(IN p_name VARCHAR(15), IN p_gender CHAR(1), IN p_age INTEGER, IN p_pin INTEGER, OUT id INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    IF p_gender != 'M' AND p_gender != 'F' THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid gender';
    ELSEIF p_age <= 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid age';
    ELSEIF p_pin < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid pin';
    ELSE
      insert into p2.customer(Name, Gender, Age, Pin) values(p_name, p_gender, p_age, p_pin);
      call p2.GET_ID(id);
      SET sql_code = 0;
    END IF;
END@
--
CREATE PROCEDURE P2.LOGIN_CHECK
(IN p_id INTEGER, IN p_pin INTEGER, OUT valid INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for select count(ID) from p2.customer where ID=p_id and Pin=p_pin;
    
    open c1;
    fetch c1 into valid;
    close c1;
END @
--
CREATE PROCEDURE P2.CUST_LOGIN
(IN p_id INTEGER, IN p_pin INTEGER, OUT valid INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    IF p_id < 100 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid id';
    ELSEIF p_pin < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid pin';
    ELSE
      call p2.LOGIN_CHECK(p_id, p_pin, valid);
      IF valid != 1 THEN
        SET valid = 0;
        SET sql_code = -100;
        SET err_msg = 'Incorrect id or pin';
      ELSE
        SET sql_code = 0;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.CUST_VALID
(IN p_id INTEGER, OUT valid INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for select count(ID) from p2.customer where ID=p_id;
    
    open c1;
    fetch c1 into valid;
    close c1;
END @
--
CREATE PROCEDURE P2.ACC_NUM
(OUT num INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for select max(Number) from p2.account;
    
    open c1;
    fetch c1 into num;
    close c1;
END @
--
CREATE PROCEDURE P2.ACCT_OPN
(IN p_id INTEGER, IN p_balance INTEGER, IN p_type CHAR(1), OUT number INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    DECLARE valid INTEGER;
    IF p_id < 100 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid id';
    ELSEIF p_balance < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid balance';
    ELSEIF p_type != 'S' AND p_type != 'C' THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid type';
    ELSE
      call p2.CUST_VALID(p_id, valid);
      IF valid != 1 THEN
        SET sql_code = -100;
        SET err_msg = 'Invalid customer id';
      ELSE
        insert into p2.account(ID, Balance, Type, Status) values(p_id, p_balance, p_type, 'A');
        call p2.ACC_NUM(number);
        SET sql_code = 0;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.ACC_STS
(IN num INTEGER, OUT valid INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for SELECT COUNT(*) FROM p2.account WHERE Status<>'I' AND Number=num;
    
    open c1;
    fetch c1 into valid;
    close c1;
END @
--
CREATE PROCEDURE P2.ACCT_CLS
(IN p_number INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    DECLARE valid INTEGER;
    IF p_number < 1000 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid number';
    ELSE
      call p2.ACC_STS(p_number, valid);
      IF valid != 1 THEN
        SET sql_code = -100;
        SET err_msg = 'Invalid account number';
      ELSE
        SET sql_code = 0;
        UPDATE p2.account SET status='I', Balance=0 WHERE Number=p_number;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_DEP
(IN p_number INTEGER, IN p_amt INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    DECLARE valid INTEGER;
    IF p_number < 1000 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid number';
    ELSEIF p_amt < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid amount';
    ELSE
      call p2.ACC_STS(p_number, valid);
      IF valid != 1 THEN
        SET sql_code = -100;
        SET err_msg = 'Invalid account number';
      ELSE
        SET sql_code = 0;
        UPDATE p2.account SET Balance=Balance+p_amt WHERE Number=p_number;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.MONEY_CHECK
(IN num INTEGER, IN amt INTEGER, OUT valid INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    declare c1 cursor for SELECT COUNT(*) FROM p2.account WHERE Number=num AND Balance>=amt;
    
    open c1;
    fetch c1 into valid;
    close c1;
END @
--
CREATE PROCEDURE P2.ACCT_WTH
(IN p_number INTEGER, IN p_amt INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    DECLARE valid INTEGER;
    DECLARE fund INTEGER;
    IF p_number < 1000 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid number';
    ELSEIF p_amt < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid amount';
    ELSE
      call p2.ACC_STS(p_number, valid);
      IF valid != 1 THEN
        SET sql_code = -100;
        SET err_msg = 'Invalid account number';
      ELSE
        call p2.MONEY_CHECK(p_number, p_amt, fund);
        IF fund != 1 THEN
          SET sql_code = -100;
          SET err_msg = 'Not enough funds';
        ELSE
          SET sql_code = 0;
          UPDATE p2.account SET Balance=Balance-p_amt WHERE Number=p_number;
        END IF;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.ACCT_TRX
(IN p_source INTEGER, IN p_dest INTEGER, IN p_amt INTEGER, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);
    DECLARE valid INTEGER;
    DECLARE fund INTEGER;
    IF p_source < 1000 OR p_dest < 1000 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid number';
    ELSEIF p_amt < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid amount';
    ELSE
      call p2.ACC_STS(p_source, valid);
      IF valid != 1 THEN
        SET sql_code = -100;
        SET err_msg = 'Invalid account number';
      ELSE
        call p2.ACC_STS(p_dest, valid);
        IF valid != 1 THEN
          SET sql_code = -100;
          SET err_msg = 'Invalid account number';
        ELSE
          call p2.MONEY_CHECK(p_source, p_amt, fund);
          IF fund != 1 THEN
            SET sql_code = -100;
            SET err_msg = 'Not enough funds';
          ELSE
            SET sql_code = 0;
            UPDATE p2.account SET Balance=Balance-p_amt WHERE Number=p_source;
            UPDATE p2.account SET Balance=Balance+p_amt WHERE Number=p_dest;
          END IF;
        END IF;
      END IF;
    END IF;
END@
--
CREATE PROCEDURE P2.ADD_INTEREST
(IN p_save_rate FLOAT, IN p_check_rate FLOAT, OUT sql_code INTEGER, OUT err_msg CHAR(100))
LANGUAGE SQL
  BEGIN
    IF p_save_rate < 0 OR p_check_rate < 0 THEN
      SET sql_code = -100;
      SET err_msg = 'Invalid rate';
    ELSE
      UPDATE p2.account SET Balance=Balance+(Balance*p_save_rate) WHERE Status<>'I' AND Type='S';
      UPDATE p2.account SET Balance=Balance+(Balance*p_check_rate) WHERE Status<>'I' AND Type='C';
      SET sql_code = 0;
    END IF;
END@
--
TERMINATE@
--
--
