/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.project.shared.dto;

import com.google.common.annotations.Beta;

import org.eclipse.che.dto.shared.DTO;

/**
 *
 * @author Ori Libhaber
 */
@Beta
@DTO
public interface CopyOptions {
    /**
     * Get value of overWrite attribute
     * @return overWrite attribute
     */
    Boolean  getOverWrite();
    /**
     * Set value of overWrite attribute
     * @param overWrite is the value to set to overWrite attribute
     */
    void setOverWrite(Boolean overWrite);
    /**
     * Get value of name attribute
     * @return name attribute
     */
    String getName();
    /**
     * Set value of name attribute
     * @param name is the value to set to name attribute
     */
    void setName(String name);
}
