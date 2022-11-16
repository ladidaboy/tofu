### 1、ascii() 查询ascii值，多个字符的情况下，显示左边第一个字符的ascii值
SELECT ASCII('abc');
-- 等同于
SELECT ASCII('a');

### 2、进制转换函数 BIN() 二进制、OCT()八进制、HEX() 十六进制
SELECT BIN(5);

### 3、CHAR() 返回数值所对应的字符
SELECT CHAR(77, 77.3, '77.3');

### 4、CONCAT(str1,str2...) 拼接字符串
SELECT CONCAT('hello','world');

### 5、字符长度函数 length()、char_length()、octet_length()
SELECT LENGTH('你好!')
UNION ALL
SELECT CHAR_LENGTH('你好!');
-- length()和char_length()区别在与，不管中西文,前者一个算一个字节，后者中文算3个字节

### 6、locate(substr,str)、instr(str,substr) 定位子串的起始位置
SELECT LOCATE('wo','hello world');

### 7、字符拼接 lpad(str1,len,str2),rpad(str1,len,str2)
SELECT LPAD('1234','6','3');
SELECT RPAD('1234','6','3');
-- len代表需要选取的字符串长度，不足的情况下用str2去填充

### 8、left(str,len)、right(str,len) 选取对应长度的字符串
SELECT LEFT('string',2);

### 9、substr(str,pos,len) = substring(str,pos,len) 截取str字符串从pos位置开始len长度的子串
SELECT SUBSTR('string',2,3);
-- 同样用法的还有mid(str,pos,len) ，功能也一个样

### 10、ltrim(str)、rtrim(str) 去除字符串左边、右边的空格

###################################################################################################

DROP TABLE `software_system`;
CREATE TABLE IF NOT EXISTS `software_system`
(
    `id`     int(11)     NOT NULL AUTO_INCREMENT,
    `name`   varchar(32) NOT NULL,
    `status` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
BEGIN;
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : Symbian',    2147483647);
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : BlackBerry', RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : Android',    RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : iOS',        RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : Harmony',    RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('M : Palm OS',    RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('D : Windows',    RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('D : Linux',      RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('D : Mac OS X',   RAND()*129);
INSERT INTO `software_system`(`name`,`status`) VALUES ('D : Unix',       NULL);
INSERT INTO `software_system`(`name`,`status`) VALUES ('D : Chrome',     NULL);
COMMIT;

-- 查询所有`status`包含1(2^0)或者4(2^2)的数据
SELECT *,LPAD(BIN(`status`),32,'.') binary_flag,BIN(`status` & 5) target_flag FROM software_system WHERE `status` & 5;
-- 查询所有`status`包含1(2^0)并且4(2^2)的数据
SELECT *,LPAD(BIN(`status`),32,'.') binary_flag,BIN(`status` & 5) target_flag FROM software_system WHERE `status` & 1 AND `status` & 4;
-- 给`status`加状态位4(2^2)
UPDATE software_system SET `status`=`status` | 4;
-- 给`status`减状态位4(2^2)
UPDATE software_system SET `status`=`status` ^ 4;
