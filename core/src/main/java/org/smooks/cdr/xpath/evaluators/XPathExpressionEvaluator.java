/*-
 * ========================LICENSE_START=================================
 * Smooks Core
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cdr.xpath.evaluators;

import org.jaxen.expr.*;
import org.jaxen.saxpath.SAXPathException;
import org.smooks.assertion.AssertArgument;
import org.smooks.cdr.xpath.SelectorStep;
import org.smooks.cdr.xpath.evaluators.equality.*;
import org.smooks.cdr.xpath.evaluators.logical.AndEvaluator;
import org.smooks.cdr.xpath.evaluators.logical.OrEvaluator;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.sax.SAXElement;
import org.w3c.dom.Element;

import java.util.Properties;

/**
 * Jaxen XPath expression evaluator.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class XPathExpressionEvaluator {

    /**
     * Does this XPath expression evaluate for the supplied {@link org.smooks.delivery.sax.SAXElement} context object.
     * <p/>
     * The implementation can update the context to a parent element if the expression targets
     * multiple contexts.
     *
     * @param element          The {@link org.smooks.delivery.sax.SAXElement} context to be evaluated against.
     * @param executionContext Smooks {@link org.smooks.container.ExecutionContext}.
     * @return True if the expression evaluates, otherwise false.
     */
    public abstract boolean evaluate(SAXElement element, ExecutionContext executionContext);

    /**
     * Does this XPath expression evaluate for the supplied {@link org.w3c.dom.Element} context object.
     * <p/>
     * The implementation can update the context to a parent element if the expression targets
     * multiple contexts.
     *
     * @param element          The {@link org.w3c.dom.Element} context to be evaluated against.
     * @param executionContext Smooks {@link org.smooks.container.ExecutionContext}.
     * @return True if the expression evaluates, otherwise false.
     */
    public abstract boolean evaluate(Element element, ExecutionContext executionContext);

    /**
     * {@link XPathExpressionEvaluator} factory method.
     * @param expr Jaxen XPath expression.
     * @param selectorStep Selector Step.
     * @param namespaces Namespace set.
     * @return The {@link XPathExpressionEvaluator} for the Jaxen expression.
     */
    public static XPathExpressionEvaluator getInstance(Expr expr, SelectorStep selectorStep, Properties namespaces) throws SAXPathException {
        AssertArgument.isNotNull(expr, "expr");

        if(expr instanceof LogicalExpr) {
            LogicalExpr logicalExpr = (LogicalExpr) expr;
            if(logicalExpr.getOperator().equalsIgnoreCase("and")) {
                return new AndEvaluator(logicalExpr, selectorStep, namespaces);
            } else if(logicalExpr.getOperator().equalsIgnoreCase("or")) {
                return new OrEvaluator(logicalExpr, selectorStep, namespaces);
            }
        } else if(expr instanceof EqualityExpr) {
            EqualityExpr equalityExpr = (EqualityExpr) expr;
            if(equalityExpr.getOperator().equalsIgnoreCase("=")) {
                return new EqualsEvaluator(equalityExpr, namespaces);
            } else if(equalityExpr.getOperator().equalsIgnoreCase("!=")) {
                return new NotEqualsEvaluator(equalityExpr, namespaces);
            }
        } else if(expr instanceof RelationalExpr) {
            RelationalExpr relationalExpr = (RelationalExpr) expr;
            if(relationalExpr.getOperator().equalsIgnoreCase("<")) {
                return new LessThanEvaluator(relationalExpr, namespaces);
            } else if(relationalExpr.getOperator().equalsIgnoreCase(">")) {
                return new GreaterThanEvaluator(relationalExpr, namespaces);
            }
        } else if(expr instanceof NumberExpr) {
            return new IndexEvaluator(((NumberExpr)expr).getNumber().intValue(), selectorStep);
        }

        throw new SAXPathException("Unsupported XPath expr token '" + expr.getText() + "'.");
    }
}
