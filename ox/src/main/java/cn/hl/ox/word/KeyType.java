package cn.hl.ox.word;

/**
 * @author hyman
 * @date 2019-09-17 10:52:24
 */
public enum KeyType {
    ERROR,
    /*关键字(50)*/
    /**关键字开始*/ KA,
    ABSTRACT,
    ASSERT,
    BOOLEAN,
    BREAK,
    BYTE,
    CASE,
    CATCH,
    CHAR,
    CLASS,
    CONST,
    CONTINUE,
    DEFAULT,
    DO,
    DOUBLE,
    ELSE,
    ENUM,
    EXTENDS,
    FINAL,
    FINALLY,
    FLOAT,
    FOR,
    GOTO,
    IF,
    IMPLEMENTS,
    IMPORT,
    INSTANCEOF,
    INT,
    INTERFACE,
    LONG,
    NATIVE,
    NEW,
    PACKAGE,
    PRIVATE,
    PROTECTED,
    PUBLIC,
    RETURN,
    SHORT,
    STATIC,
    STRICTFP,
    SUPER,
    SWITCH,
    SYNCHRONIZED,
    THIS,
    THROW,
    THROWS,
    TRANSIENT,
    TRY,
    VOID,
    VOLATILE,
    WHILE,
    /**关键字开始*/ KZ,
    /*运算符(10)*/
    PLUS,
    MIN,
    MUL,
    DIV,
    AND,
    OR,
    NOT,
    EQ,
    LT,
    GT,
    /** 数字 */
    DIGIT,
    /** 分界符 → ',', ';', '{', '}', '(', ')', '[', ']', '_', ':', '.', '"' */
    SEPARATORS,
    /** 换行符 → \n */
    WRAP,
    /** 标识符 */
    ID,
    /** 字符串 */
    STRING,
    /** 注释 */
    COMMENT;
}
