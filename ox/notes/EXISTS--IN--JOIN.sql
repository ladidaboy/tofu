mysql -u root -p                                                # 进入MySQL数据库控制台
123456                                                          # 输入数据库密码
show databases;                                                 # 查看数据库
create database ax;                                             # 创建新数据库 ax
use ax;                                                         # 进入ax数据库目录
drop database ax;                                               # 删除数据库 ax
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -#- -- -- -- -- -- -- -- -- -- -- --
-- 创建表
DROP TABLE IF EXISTS MILLION_DATA;
CREATE TABLE MILLION_DATA (
  UNIQUE_INDEXED_NOTNULL INT NOT NULL, -- 无重复，有索引，不可空值
  UNIQUE_NOINDEX_NOTNULL INT NOT NULL, -- 无重复，无索引，不可空值
  REPEAT_INDEXED_NOTNULL INT NOT NULL, -- 有重复，有索引，不可空值
  REPEAT_NOINDEX_NOTNULL INT NOT NULL, -- 有重复，无索引，不可空值
  UNIQUE_INDEXED_CANNULL INT NULL,     -- 无重复，有索引，可以空值
  UNIQUE_NOINDEX_CANNULL INT NULL,     -- 无重复，无索引，可以空值
  REPEAT_INDEXED_CANNULL INT NULL,     -- 有重复，有索引，可以空值
  REPEAT_NOINDEX_CANNULL INT NULL,     -- 有重复，无索引，可以空值

  REMARK VARCHAR(32),
  PRIMARY KEY (UNIQUE_INDEXED_NOTNULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 创建索引
CREATE UNIQUE INDEX UNI_IDX_1 ON MILLION_DATA(UNIQUE_INDEXED_NOTNULL);
ALTER TABLE MILLION_DATA ADD UNIQUE UNI_1 (UNIQUE_NOINDEX_NOTNULL);
ALTER TABLE MILLION_DATA ADD INDEX (REPEAT_INDEXED_NOTNULL);
CREATE UNIQUE INDEX UNI_IDX_2 ON MILLION_DATA(UNIQUE_INDEXED_CANNULL);
ALTER TABLE MILLION_DATA ADD UNIQUE UNI_2 (UNIQUE_NOINDEX_CANNULL);
ALTER TABLE MILLION_DATA ADD INDEX (REPEAT_INDEXED_CANNULL);
DROP INDEX UNI_1 ON MILLION_DATA;
DROP INDEX UNI_2 ON MILLION_DATA;
SHOW INDEX FROM MILLION_DATA;

-- 初始化数据
DELIMITER $$
DROP PROCEDURE IF EXISTS PROC_BATCH_INSERT;
CREATE PROCEDURE PROC_BATCH_INSERT()
BEGIN
DECLARE iMax INT;
DECLARE iIdx INT;
SET iMax=1000000;
SET iIdx=1;
WHILE iIdx < iMax DO
    INSERT INTO MILLION_DATA (
        UNIQUE_INDEXED_NOTNULL,
        UNIQUE_NOINDEX_NOTNULL,
        REPEAT_INDEXED_NOTNULL,
        REPEAT_NOINDEX_NOTNULL,
        UNIQUE_INDEXED_CANNULL,
        UNIQUE_NOINDEX_CANNULL,
        REPEAT_INDEXED_CANNULL,
        REPEAT_NOINDEX_CANNULL,
        REMARK
    )
    VALUES  (
        iIdx,
        iMax - iIdx,
        iIdx % (iMax / 10000),
        iMax % iIdx,
        iIdx,
        iMax - iIdx,
        iIdx % (iMax / 10000),
        iMax % iIdx,
        LEFT(CONCAT('RN',CAST(iIdx AS CHAR),REPEAT('*', 30)), 32)
    );
SET iIdx=iIdx+1;
END WHILE;
SET iMax=iMax+500;
WHILE iIdx < iMax DO
    INSERT INTO MILLION_DATA (
        UNIQUE_INDEXED_NOTNULL,
        UNIQUE_NOINDEX_NOTNULL,
        REPEAT_INDEXED_NOTNULL,
        REPEAT_NOINDEX_NOTNULL,
        REMARK
    )
    VALUES  (
        iIdx,
        iMax - iIdx,
        iIdx % (iMax / 10000),
        iMax % iIdx,
        LEFT(CONCAT('RN',CAST(iIdx AS CHAR),REPEAT('*', 30)), 32)
    );
SET iIdx=iIdx+1;
END WHILE;
END $$
DELIMITER ;
CALL PROC_BATCH_INSERT();
DROP PROCEDURE IF EXISTS PROC_BATCH_INSERT;
SELECT * FROM (SELECT REPEAT_NOINDEX_NOTNULL, COUNT(1) CNT FROM MILLION_DATA GROUP BY REPEAT_NOINDEX_NOTNULL) T WHERE T.CNT > 32;


-- 表中字段不允许NULL -------------------------------------------------
-- TestCase1: 无重复数据，无索引
DROP TABLE IF EXISTS T1;
DROP TABLE IF EXISTS T2;
CREATE TABLE T1(n int NOT NULL);
CREATE TABLE T2(n int NOT NULL);
INSERT INTO T1 SELECT n FROM dbo.Nums WHERE n <= 100;
INSERT INTO T2 SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 2 = 0;

-- TestCase2: 无重复数据，有索引
CREATE UNIQUE INDEX IX_T1 ON T1(n);
CREATE UNIQUE INDEX IX_T2 ON T2(n);

-- TestCase3: 有重复数据，无索引
DROP TABLE IF EXISTS T1;
DROP TABLE IF EXISTS T2;
CREATE TABLE T1(n int NOT NULL);
CREATE TABLE T2(n int NOT NULL);
INSERT INTO T1 SELECT n FROM dbo.Nums WHERE n <= 100;
INSERT INTO T2 SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 2 = 0
UNION ALL
SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 3 = 0;

-- TestCase4: 有重复数据，有索引
CREATE INDEX IX_T1 ON T1(n);
CREATE INDEX IX_T2 ON T2(n);

-- 表中字段允许NULL --------------------------------------------------
-- TestCase5: 无重复数据，无索引
DROP TABLE IF EXISTS T1;
DROP TABLE IF EXISTS T2;
CREATE TABLE T1(n int NULL);
CREATE TABLE T2(n int NULL);
INSERT INTO T1 SELECT n FROM dbo.Nums WHERE n <= 100;
INSERT INTO T2 SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 2 = 0;

-- TestCase6: 无重复数据，有索引
CREATE UNIQUE INDEX IX_T1 ON T1(n);
CREATE UNIQUE INDEX IX_T2 ON T2(n);

-- TestCase7: 有重复数据，无索引
DROP TABLE IF EXISTS T1;
DROP TABLE IF EXISTS T2;
CREATE TABLE T1(n int NULL);
CREATE TABLE T2(n int NULL);
INSERT INTO T1 SELECT n FROM dbo.Nums WHERE n <= 100;
INSERT INTO T2 SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 2 = 0
UNION ALL
SELECT n FROM dbo.Nums WHERE n <= 100 AND n % 3 = 0;

-- TestCase8: 有重复数据，有索引
CREATE INDEX IX_T1 ON T1(n);
CREATE INDEX IX_T2 ON T2(n);

-- Foreach TestCase above，分别执行以下两组语句并观察执行计划：--------------------
-- 肯定式逻辑
SELECT T1.* FROM T1 WHERE EXISTS (SELECT * FROM T2 WHERE T2.n = T1.n);
SELECT T1.* FROM T1 WHERE T1.n IN (SELECT T2.n FROM T2);
SELECT DISTINCT T1.* /*不加DISTINCT可能会引起重复*/ FROM T1 INNER JOIN T2 ON T1.n = T2.n;

-- 否定式逻辑
SELECT T1.* FROM T1 WHERE NOT EXISTS (SELECT * FROM T2 WHERE T2.n = T1.n);
SELECT T1.* FROM T1 WHERE T1.n NOT IN (SELECT T2.n FROM T2);
SELECT T1.* FROM T1 LEFT JOIN T2 ON T1.n = T2.n WHERE T2.n IS NULL;
-- End Foreach

-- 清场 ----------------------------------------------------------
DROP TABLE IF EXISTS T1;
DROP TABLE IF EXISTS T2;
