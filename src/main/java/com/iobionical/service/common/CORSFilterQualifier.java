/*
 * Copyright (c) 2018 Sami. All Rights Reserved. Company Confidential.
 */
package com.iobionical.service.common;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sami
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface CORSFilterQualifier {
}
