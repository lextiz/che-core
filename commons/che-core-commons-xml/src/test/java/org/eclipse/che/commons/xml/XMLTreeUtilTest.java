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
package org.eclipse.che.commons.xml;


import org.testng.annotations.Test;


import static org.eclipse.che.commons.xml.XMLTreeUtil.closeTagLength;
import static org.eclipse.che.commons.xml.XMLTreeUtil.indexOf;
import static org.eclipse.che.commons.xml.XMLTreeUtil.indexOfAttributeName;
import static org.eclipse.che.commons.xml.XMLTreeUtil.replaceAll;
import static org.eclipse.che.commons.xml.XMLTreeUtil.rootStart;
import static org.eclipse.che.commons.xml.XMLTreeUtil.single;
import static org.eclipse.che.commons.xml.XMLTreeUtil.insertBetween;
import static org.eclipse.che.commons.xml.XMLTreeUtil.insertInto;
import static org.eclipse.che.commons.xml.XMLTreeUtil.lastIndexOf;
import static org.eclipse.che.commons.xml.XMLTreeUtil.openTagLength;
import static org.eclipse.che.commons.xml.XMLTreeUtil.tabulate;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * Tests for {@link XMLTreeUtil}
 *
 * @author Eugene Voevodin
 */
public class XMLTreeUtilTest {

    @Test
    public void shouldTabulateOneLineString() {
        final String src = "text here";

        assertEquals(tabulate(src, 2), "        " + src);
    }

    @Test
    public void shouldTabulateMultilineString() {
        final String src = "first line\nsecond line";

        assertEquals(tabulate(src, 1), "    first line\n    second line");
    }

    @Test
    public void shouldReturnFirstElement() {
        assertEquals(single(asList("first")), "first");
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionWhenListContainsNotOnlyElement() {
        single(asList("first", "second"));
    }

    @Test
    public void shouldInsertContentBetweenTwoAnchors() {
        //                        6     12
        final byte[] src = "<name>content</name>".getBytes();

        final byte[] newSrc = insertBetween(src, 6, 12, "new content");

        assertEquals(newSrc, "<name>new content</name>".getBytes());
    }

    @Test
    public void shouldInsertContentToCharArrayInSelectedPlace() {
        //                        6
        final byte[] src = "<name></name>".getBytes();

        final byte[] newSrc = insertInto(src, 6, "new content");

        assertEquals(new String(newSrc).intern(), "<name>new content</name>");
    }

    @Test
    public void shouldBeAbleToFindLastIndexOf() {
        //                             11        20      28
        final byte[] src = "...</before>\n       <current>...".getBytes();

        assertEquals(lastIndexOf(src, '>', 20), 11);
        assertEquals(lastIndexOf(src, '>', src.length - 1), 28);
    }

    @Test
    public void shouldBeAbleToGetElementOpenTagLength() {
        //<test>test</test>
        final NewElement newElement = NewElement.createElement("test", "test");

        assertEquals(openTagLength(newElement), 6);
    }

    @Test
    public void shouldBeAbleToGetElementCloseTagLength() {
        //<test>test</test>
        final NewElement newElement = NewElement.createElement("test", "test");

        assertEquals(closeTagLength(newElement), 7);
    }

    @Test
    public void shouldBeAbleToGetIndexOf() {
        final String src = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOf(byteSrc, "attribute1".getBytes(), 0), src.indexOf("attribute1"));
        assertEquals(indexOf(byteSrc, "attribute2".getBytes(), 0), src.indexOf("attribute2"));
        assertEquals(indexOf(byteSrc, "attribute3".getBytes(), 0), src.indexOf("attribute3"));
    }

    @Test
    public void shouldReturnMinusOneIfTargetBytesWereNotFound() {
        final String src = "source string";
        final byte[] byteSrc = src.getBytes();

        assertNotEquals(indexOf(byteSrc, "string".getBytes(), 0), -1);
        assertEquals(indexOf(byteSrc, "strings".getBytes(), 0), -1);
    }

    @Test
    public void shouldBeAbleToFindIndexOfAttributeNameBytes() {
        final String src = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOfAttributeName(byteSrc, "attribute1".getBytes(), 0), src.indexOf("attribute1"));
        assertEquals(indexOfAttributeName(byteSrc, "attribute2".getBytes(), 0), src.indexOf("attribute2"));
        assertEquals(indexOfAttributeName(byteSrc, "attribute3".getBytes(), 0), src.indexOf("attribute3"));
    }

    @Test
    public void shouldReturnMinusOneIfAttributeNameBytesWereNotFound() {
        final String src = "<element attribute12=\"value1\"/>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOfAttributeName(byteSrc, "attribute1".getBytes(), 0), -1);
    }

    @Test
    public void shouldBeAbleToReplaceAllBytesWithGivenBytes() {
        final byte[] src = "\r text \r".getBytes();

        final byte[] newSrc = XMLTreeUtil.replaceAll(src, (byte)'\r', "&#xD;".getBytes(), 0);

        assertEquals(newSrc, "&#xD; text &#xD;".getBytes());
    }

    @Test
    public void shouldBeAbleToReplaceAllBytesWithGivenBytesStartingFromSpecifiedIndex() {
        final byte[] src = "\r \r text \r \r".getBytes();

        final byte[] newSrc = XMLTreeUtil.replaceAll(src, (byte)'\r', "&#xD;".getBytes(), 5);

        assertEquals(newSrc, "\r \r text &#xD; &#xD;".getBytes());
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenReplacingBytesWithStartFromParamWhichIsOutOfSourceBounds() {
        replaceAll("abc".getBytes(), (byte)'a', "ab".getBytes(), 3);
    }

    @Test
    public void shouldReturnSameArrayWhenSourceBytesDoNotContainTargetByte() {
        final byte[] src = "text".getBytes();

        final byte[] newSrc = XMLTreeUtil.replaceAll(src, (byte)'\r', "&#xD;".getBytes(), 0);

        assertEquals(newSrc, "text".getBytes());
    }

    @Test
    public void shouldFindRootStart() {
        final String src = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                           "<!-- Comment -->" +
                           "<!-- Another comment -->" +
                           "<root></root>";

        int start = rootStart(src.getBytes());

        assertEquals(start, src.indexOf("<root>"));
    }

    @Test
    public void shouldFindRootStartEvenIfCommentContainsStartCharacter() {
        final String src = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                           "<!-- Comment < < -->" +
                           "<!-- Another < < comment -->" +
                           "<root></root>";

        int start = rootStart(src.getBytes());

        assertEquals(start, src.indexOf("<root>"));
    }
}
