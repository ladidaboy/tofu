package cn.hl.ax.spring.base.swagger;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.parameter.Examples;
import springfox.documentation.swagger.readers.parameter.SwaggerExpandedParameterBuilder;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @see SwaggerExpandedParameterBuilder
 * @author hyman
 * @date 2019-12-27 13:27:41
 */
@Slf4j
@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 9999)
public class MySwaggerExpandedParameterBuilder implements ExpandedParameterBuilderPlugin {
    private static final String NUMBER_TYPES = "integer,float,double,long,decimal";
    private static final String READONLY_TAG = "<font color=red>[ReadOnly]</font>";

    private final DescriptionResolver descriptions;
    private final EnumTypeDeterminer  enumTypeDeterminer;

    /**
     * [ORIGINAL]
     */
    @Autowired
    public MySwaggerExpandedParameterBuilder(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
        this.descriptions = descriptions;
        this.enumTypeDeterminer = enumTypeDeterminer;
        log.info("CustomSwaggerExpandedParameterBuilder init!");
    }

    /**
     * [ORIGINAL]
     */
    @Override
    public void apply(ParameterExpansionContext context) {
        Optional<ApiModelProperty> apiModelPropertyOptional = context.findAnnotation(ApiModelProperty.class);
        if (apiModelPropertyOptional.isPresent()) {
            fromApiModelProperty(context, apiModelPropertyOptional.get());
        }
        Optional<ApiParam> apiParamOptional = context.findAnnotation(ApiParam.class);
        if (apiParamOptional.isPresent()) {
            fromApiParam(context, apiParamOptional.get());
        }
    }

    /**
     * [ORIGINAL]
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    /**
     * [ORIGINAL]
     */
    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        String allowableProperty = Strings.emptyToNull(apiParam.allowableValues());
        AllowableValues allowable = allowableValues(//
                Optional.fromNullable(allowableProperty),//
                context.getFieldType().getErasedType());//

        maybeSetParameterName(context, apiParam.name())//
                .description(descriptions.resolve(apiParam.value()))//
                .defaultValue(apiParam.defaultValue())//
                .required(apiParam.required())//
                .allowMultiple(apiParam.allowMultiple())//
                .allowableValues(allowable)//
                .parameterAccess(apiParam.access())//
                .hidden(apiParam.hidden())//
                .scalarExample(apiParam.example())//
                .complexExamples(Examples.examples(apiParam.examples()))//
                .order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)//
                .build();
    }

    /**
     * [CUSTOMIZED]
     */
    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = Strings.emptyToNull(apiModelProperty.allowableValues());
        AllowableValues allowable = allowableValues(//
                Optional.fromNullable(allowableProperty),//
                context.getFieldType().getErasedType());//

        // 定制化处理
        AccessMode accessMode = apiModelProperty.accessMode();
        String value = apiModelProperty.value();
        if (AccessMode.READ_ONLY == accessMode) {
            value = READONLY_TAG + value;
        }
        String dataType = apiModelProperty.dataType();
        String example = apiModelProperty.example();
        if (StrUtil.isNotEmpty(dataType) && StrUtil.containsIgnoreCase(NUMBER_TYPES, dataType)) {
            if (StrUtil.isEmpty(example)) {
                example = "0";
            }
        }

        maybeSetParameterName(context, apiModelProperty.name())//name
                .description(descriptions.resolve(value))//description
                //defaultValue
                .required(apiModelProperty.required())//required
                //allowMultiple
                .allowableValues(allowable)//allowableValues
                //paramType
                .parameterAccess(apiModelProperty.accessMode().name())//paramAccess
                //type
                //modelRef
                .hidden(apiModelProperty.hidden())//hidden
                //pattern
                //vendorExtensions {ArrayList}
                //collectionFormat
                //allowEmptyValue
                .order(apiModelProperty.position()) //order
                .scalarExample(example)//scalarExample
                //examples{LinkedListMultimap}
                .build();
    }

    /**
     * [ORIGINAL]
     */
    private ParameterBuilder maybeSetParameterName(ParameterExpansionContext context, String parameterName) {
        if (!Strings.isNullOrEmpty(parameterName)) {
            context.getParameterBuilder().name(parameterName);
        }
        return context.getParameterBuilder();
    }

    /**
     * [ORIGINAL]
     */
    private AllowableValues allowableValues(final Optional<String> optionalAllowable, Class<?> fieldType) {

        AllowableValues allowable = null;
        if (enumTypeDeterminer.isEnum(fieldType)) {
            allowable = new AllowableListValues(getEnumValues(fieldType), "LIST");
        } else if (optionalAllowable.isPresent()) {
            allowable = ApiModelProperties.allowableValueFromString(optionalAllowable.get());
        }
        return allowable;
    }

    /**
     * [ORIGINAL]
     */
    private List<String> getEnumValues(final Class<?> subject) {
        return Lists.transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() {
            @Override
            public String apply(final Object input) {
                return input.toString();
            }
        });
    }
}
