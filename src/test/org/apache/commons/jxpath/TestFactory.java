/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jxpath/src/test/org/apache/commons/jxpath/Attic/TestFactory.java,v 1.2 2002/04/21 21:52:34 dmitri Exp $
 * $Revision: 1.2 $
 * $Date: 2002/04/21 21:52:34 $
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2001, Plotnix, Inc,
 * <http://www.plotnix.com/>.
 * For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.commons.jxpath;

import java.util.*;
import org.w3c.dom.*;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.2 $ $Date: 2002/04/21 21:52:34 $
 */
public class TestFactory extends AbstractFactory {

    /**
     * Create a new instance and put it in the collection on the parent object.
     * Return the created object or <b>null</b> if this factory cannot create
     * the requested object.
     */
    public boolean createObject(JXPathContext context, Pointer pointer, Object parent, String name, int index){
        if (name.equals("testArray")){
            ((TestBean[])parent)[index] = new TestBean();
            return true;
        }
        else if (name.equals("strings")){
            NestedTestBean bean = (NestedTestBean)parent;
            bean.setStrings(new String[index + 1]);
            bean.getStrings()[index] = "";
            return true;
        }
        else if (name.equals("nestedBean")){
            ((TestBean)parent).setNestedBean(new NestedTestBean("newName"));
            return true;
        }
        else if (name.equals("beans")){
            TestBean bean = (TestBean)parent;
            if (bean.getBeans() == null || index >= bean.getBeans().length){
                bean.setBeans(new NestedTestBean[index + 1]);
            }
            ((TestBean)parent).getBeans()[index] = new NestedTestBean("newName");
            return true;
        }
        else if (name.equals("map")){
            ((TestBean)parent).setMap(new HashMap());
            return true;
        }
        else if (name.equals("TestKey2")){
            ((Map)parent).put(name, new NestedTestBean("newName"));
            return true;
        }
        else if (name.equals("TestKey3")){
            ((Map)parent).put(name, new Vector());
            return true;
        }
        else if (name.equals("TestKey4")){
            ((Map)parent).put(name, new Object[]{new TestBean()});
            return true;
        }
        else if (name.equals("TestKey5")){
            TestBean tb = new TestBean();
            tb.setNestedBean(null);
            tb.setBeans(null);
            ((Map)parent).put(name, tb);
            return true;
        }
        else if (name.equals("location") || name.equals("address") || name.equals("street")){
            addElement((Node)parent, index, name);
            return true;
        }
        return false;
    }

    private void addElement(Node parent, int index, String tag){
        boolean repeat = true;
        while(repeat){
            Node child = parent.getFirstChild();
            int count = 0;
            while (child != null){
                if (child.getNodeName().equals(tag)){
                    if (count == index){
                        repeat = false;
                        break;
                    }
                    count++;
                }
                child = child.getNextSibling();
            }
            if (child != null){
                child = child.getNextSibling();
            }
            Node newElement = parent.getOwnerDocument().createElement(tag);
            if (child != null){
                parent.insertBefore(newElement, child);
            }
            else {
                parent.appendChild(newElement);
            }
        }
    }

    /**
     * Create a new object and set it on the specified variable
     */
    public boolean declareVariable(JXPathContext context, String name){
        if (name.equals("test")){
            context.getVariables().declareVariable(name, new TestBean());
            return true;
        }
        else if (name.equals("testArray")){
            context.getVariables().declareVariable(name, new TestBean[0]);
            return true;
        }
        else if (name.equals("stringArray")){
            context.getVariables().declareVariable(name, new String[]{"Value1"});
            return true;
        }
        context.getVariables().declareVariable(name, null);
        return true;
    }
}