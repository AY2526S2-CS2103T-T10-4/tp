package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PersonListPanelTest {

    @Test
    public void calculateScrollIndexAfterClearingSelection_emptyList_returnsNoScrollIndex() {
        assertEquals(-1, PersonListPanel.calculateScrollIndexAfterClearingSelection(3, 0));
    }

    @Test
    public void calculateScrollIndexAfterClearingSelection_outOfBoundsIndex_clampsToLastItem() {
        assertEquals(1, PersonListPanel.calculateScrollIndexAfterClearingSelection(3, 2));
    }

    @Test
    public void calculateScrollIndexAfterClearingSelection_noPreviousSelection_scrollsToTop() {
        assertEquals(0, PersonListPanel.calculateScrollIndexAfterClearingSelection(-1, 2));
    }
}
