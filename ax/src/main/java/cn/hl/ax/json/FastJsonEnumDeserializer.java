package cn.hl.ax.json;

import cn.hl.ax.enums.BaseEnumInterface;
import cn.hl.ax.enums.EnumUtils;
import cn.hl.ax.log.LogUtils;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

/**
 * @author hyman
 * @date 2020-08-22 13:32:52
 */
@Slf4j
public class FastJsonEnumDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        Class cls = (Class) type;
        if (BaseEnumInterface.class.isAssignableFrom(cls)) {
            try {
                return (T) EnumUtils.fromField(cls, "value", lexer.intValue());
            } catch (Exception e) {
                log.error("EnumDeserializer`{}` by value error!\n{}", fieldName, LogUtils.getSimpleMessages(e));
            }
        } else {
            //没实现BaseEnumInterface接口的,默认按`name`或者按`ordinal`匹配
            if (token == JSONToken.LITERAL_INT) {
                lexer.nextToken(JSONToken.COMMA);
                try {
                    return (T) EnumUtils.fromOrdinal(cls, lexer.intValue());
                } catch (Exception e) {
                    log.error("EnumDeserializer`{}` by ordinal error!\n{}", fieldName, LogUtils.getSimpleMessages(e));
                }
            } else if (token == JSONToken.LITERAL_STRING) {
                try {
                    return (T) EnumUtils.fromName(cls, lexer.stringVal());
                } catch (Exception e) {
                    log.error("EnumDeserializer`{}` by name error!\n{}", fieldName, LogUtils.getSimpleMessages(e));
                }
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
