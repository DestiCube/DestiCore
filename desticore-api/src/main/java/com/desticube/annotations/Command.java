package com.desticube.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	public String command() default "";
	public String[] aliases() default "";
	public String description() default "";
	public String[] permissions() default "";
}
