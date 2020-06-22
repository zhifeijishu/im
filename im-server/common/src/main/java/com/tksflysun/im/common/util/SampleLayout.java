package com.tksflysun.im.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tksflysun.im.common.result.ControllerResult;

@Plugin(name = "SampleLayout", category = "Core", elementType = "layout", printObject = true)
public class SampleLayout extends AbstractStringLayout {

    protected SampleLayout(boolean locationInfo, boolean properties, boolean complete, Charset charset) {
        super(charset);
    }

    @PluginFactory
    public static SampleLayout createLayout(@PluginAttribute("locationInfo") boolean locationInfo,
        @PluginAttribute("properties") boolean properties, @PluginAttribute("complete") boolean complete,
        @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
        return new SampleLayout(locationInfo, properties, complete, charset);
    }

    @Override
    public String toSerializable(LogEvent loggingEvent) {
        if (loggingEvent.getMessage().getParameters() == null
            || loggingEvent.getMessage().getParameters().length == 0) {
            return String.format("[%s] [%s] %s", "elk", DateUtil.formatDate(new Date()), "不能解析的日志类型" + "\n");
        }
        StringBuilder exceptionBuiler = new StringBuilder();
        Object object = loggingEvent.getMessage().getParameters()[0];

        JSONObject logstashEvent = new JSONObject();
        if (loggingEvent.getThrown() != null) {
            final Throwable throwableInformation = loggingEvent.getThrown();
            try {
                Writer w = new StringWriter();
                throwableInformation.printStackTrace(new PrintWriter(w));
                exceptionBuiler.append(w.toString());
            } catch (Exception e) {
            }
        } else if ("ERROR".equals(loggingEvent.getLevel().toString())) {
            if (object instanceof Exception) {
                Exception exception = (Exception)object;
                String stackTrace = ExceptionUtils.getMessage(exception);
                exceptionBuiler.append(stackTrace);
            }
        }

        if (loggingEvent.isIncludeLocation()) {
            StackTraceElement stackTraceElement = loggingEvent.getSource();
            logstashEvent.put("exceptionName", String.format("%s %s %s", stackTraceElement.getClassName(),
                stackTraceElement.getMethodName(), stackTraceElement.getLineNumber()));
        }

        logstashEvent.put("exceptionLevel", loggingEvent.getLevel().toString());

        if (String.class.isAssignableFrom(object.getClass())) {
            exceptionBuiler.append(object);
        } else if (Exception.class.isAssignableFrom(object.getClass())) {
            Exception exception = (Exception)object;
            exceptionBuiler.append(ExceptionUtils.getMessage(exception));
            logstashEvent.put("code", ControllerResult.ERROR);
        } else {

        }
        logstashEvent.put("exception", exceptionBuiler.toString());
        return String.format("[%s] [%s] %s", "elk", DateUtil.formatDate(new Date()),
            JSON.toJSONString(logstashEvent) + "\n");
    }
}
