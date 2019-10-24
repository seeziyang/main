package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BORROWER_ID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOAN_ID;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_BOOKS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalBooks.BOOK_1;
import static seedu.address.testutil.TypicalBooks.BOOK_2;
import static seedu.address.testutil.TypicalBooks.getTypicalCatalog;
import static seedu.address.testutil.TypicalBorrowers.ALICE;
import static seedu.address.testutil.TypicalBorrowers.BOB;
import static seedu.address.testutil.TypicalBorrowers.getTypicalBorrowerRecords;
import static seedu.address.testutil.TypicalLoans.LOAN_1;
import static seedu.address.testutil.TypicalLoans.getTypicalLoanRecords;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.UserSettings;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.RegisterCommand;
import seedu.address.logic.commands.ServeCommand;
import seedu.address.model.book.Book;
import seedu.address.model.book.BookPredicate;
import seedu.address.model.borrower.BorrowerId;
import seedu.address.model.exceptions.NotInServeModeException;
import seedu.address.model.loan.Loan;
import seedu.address.model.loan.LoanId;
import seedu.address.testutil.BookBuilder;
import seedu.address.testutil.CatalogBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new Catalog(), new Catalog(modelManager.getCatalog()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setCatalogFilePath(Paths.get("catalog/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setCatalogFilePath(Paths.get("new/catalog/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setUserSettings_nullUserSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserSettings(null));
    }

    @Test
    public void setUserSettings_validUserSettings_setsUserSettings() {
        UserSettings userSettings = new UserSettings(10, 10, 10);
        modelManager.setUserSettings(userSettings);
        assertEquals(userSettings, modelManager.getUserSettings());
    }

    @Test
    public void setCatalogFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setCatalogFilePath(null));
    }

    @Test
    public void setCatalogFilePath_validPath_setsCatalogFilePath() {
        Path path = Paths.get("catalog/file/path");
        modelManager.setCatalogFilePath(path);
        assertEquals(path, modelManager.getCatalogFilePath());
    }

    @Test
    public void setLoanRecordsFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setLoanRecordsFilePath(null));
    }

    @Test
    public void setLoanRecordsFilePath_validPath_setsLoanRecordsFilePath() {
        Path path = Paths.get("loan/record/file/path");
        modelManager.setLoanRecordsFilePath(path);
        assertEquals(path, modelManager.getLoanRecordsFilePath());
    }

    @Test
    public void setBorrowerRecordsFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setBorrowerRecordsFilePath(null));
    }

    @Test
    public void setBorrowerRecordsFilePath_validPath_setsBorrowerRecordsFilePath() {
        Path path = Paths.get("borrower/record/file/path");
        modelManager.setBorrowerRecordsFilePath(path);
        assertEquals(path, modelManager.getBorrowerRecordsFilePath());
    }

    @Test
    public void hasBook_nullBook_throwsNullPointerException() {
        Book book = null;
        assertThrows(NullPointerException.class, () -> modelManager.hasBook(book));
    }

    @Test
    public void hasBook_bookNotInCatalog_returnsFalse() {
        assertFalse(modelManager.hasBook(BOOK_1));
    }

    @Test
    public void hasBook_bookInCatalog_returnsTrue() {
        modelManager.addBook(BOOK_1);
        assertTrue(modelManager.hasBook(BOOK_1));
    }

    @Test
    public void getFilteredBookList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredBookList().remove(0));
    }

    @Test
    public void getOverdueBooks_success() {
        modelManager = new ModelManager();

        LoanId loanId = new LoanId(VALID_LOAN_ID);
        Book toBeLoaned = new BookBuilder(BOOK_1).build();
        BorrowerId currentBorrowerId = new BorrowerId(VALID_BORROWER_ID);
        Loan loan = new Loan(loanId, toBeLoaned.getSerialNumber(), currentBorrowerId,
                DateUtil.getTodayMinusDays(31), DateUtil.getTodayMinusDays(1));
        Book loanedBook = new BookBuilder(BOOK_1).withLoan(loan).build();
        modelManager.addBook(loanedBook);
        modelManager.addBook(BOOK_2);
        List<Book> overdueBooks = modelManager.getOverdueBooks();
        List<Book> expectedBooks = modelManager.getCatalog()
                .getBookList()
                .stream()
                .filter(book -> book.equals(BOOK_1))
                .collect(Collectors.toList());
        assertEquals(overdueBooks, expectedBooks);
    }

    @Test
    public void getOverdueBooks_noOverdueBooks_returnEmptyList() {
        modelManager = new ModelManager();

        // Book on loan but not overdue
        LoanId loanId = new LoanId(VALID_LOAN_ID);
        Book toBeLoaned = new BookBuilder(BOOK_1).build();
        BorrowerId currentBorrowerId = new BorrowerId(VALID_BORROWER_ID);
        Loan loan = new Loan(loanId, toBeLoaned.getSerialNumber(), currentBorrowerId,
                DateUtil.getTodayDate(), DateUtil.getTodayPlusDays(30));
        modelManager.addLoan(loan);
        Book loanedBook = new BookBuilder(BOOK_1).withLoan(loan).build();
        modelManager.addBook(loanedBook);
        modelManager.addBook(BOOK_2);
        List<Book> overdueBooks = modelManager.getOverdueBooks();
        assertTrue(overdueBooks.isEmpty());

        // No books on loan
        modelManager.deleteBook(loanedBook);
        modelManager.addBook(BOOK_1);
        overdueBooks = modelManager.getOverdueBooks();
        assertTrue(overdueBooks.isEmpty());
    }

    @Test
    public void getServingBorrower_notInServeMode_throwsNotInServeModeException() {
        modelManager = new ModelManager();

        assertThrows(NotInServeModeException.class, () -> modelManager.getServingBorrower());
    }

    @Test
    public void getBorrowerBooks_notInServeMode_throwsNotInServeModeException() {
        modelManager = new ModelManager();

        assertThrows(NotInServeModeException.class, () -> modelManager.getBorrowerBooks());
    }

    @Test
    public void getBorrowerBooks_noBooksBorrowed_returnEmptyList() throws Exception {
        Model modelManager = new ModelManager();
        new RegisterCommand(BOB).execute(modelManager);

        BorrowerId validBorrowerId = new BorrowerId(VALID_ID_BOB);
        new ServeCommand(validBorrowerId).execute(modelManager);

        assertTrue(modelManager.getBorrowerBooks().isEmpty());
    }

    @Test
    public void servingBorrowerNewLoan_inServeMode_success() {
        Model modelManager = new ModelManager(getTypicalCatalog(), getTypicalLoanRecords(),
                getTypicalBorrowerRecords(), new UserPrefs());

        modelManager.setServingBorrower(ALICE);
        modelManager.servingBorrowerNewLoan(LOAN_1);
        assertTrue(modelManager.getServingBorrower().hasCurrentLoan(LOAN_1));
    }

    @Test
    public void servingBorrowerNewLoan_notInServeMode_throwsNotInServeModeException() {
        Model modelManager = new ModelManager();

        assertThrows(NotInServeModeException.class, () ->
                modelManager.servingBorrowerNewLoan(LOAN_1));
    }

    @Test
    public void servingBorrowerReturnLoan_inServeMode_success() {
        Model modelManager = new ModelManager(getTypicalCatalog(), getTypicalLoanRecords(),
                getTypicalBorrowerRecords(), new UserPrefs());

        modelManager.setServingBorrower(ALICE);
        modelManager.servingBorrowerNewLoan(LOAN_1);
        modelManager.servingBorrowerReturnLoan(LOAN_1);
        assertFalse(modelManager.getServingBorrower().hasCurrentLoan(LOAN_1));
    }

    @Test
    public void servingBorrowerReturnLoan_notInServeMode_throwsNotInServeModeException() {
        Model modelManager = new ModelManager();

        assertThrows(NotInServeModeException.class, () ->
                modelManager.servingBorrowerReturnLoan(LOAN_1));
    }

    @Test
    public void equals() {
        Catalog catalog = new CatalogBuilder().withPerson(BOOK_1).withPerson(BOOK_2).build();
        Catalog catalogCopy = new CatalogBuilder().withPerson(BOOK_1).withPerson(BOOK_2).build();
        Catalog differentCatalog = new Catalog();
        UserPrefs userPrefs = new UserPrefs();
        UserPrefs userPrefsCopy = new UserPrefs();
        LoanRecords loanRecords = new LoanRecords();
        LoanRecords loanRecordsCopy = new LoanRecords();
        BorrowerRecords borrowerRecords = getTypicalBorrowerRecords();
        BorrowerRecords borrowerRecordsCopy = getTypicalBorrowerRecords();

        // same values -> returns true
        modelManager = new ModelManager(catalog, loanRecords, borrowerRecords, userPrefs);
        ModelManager modelManagerCopy =
                new ModelManager(catalogCopy, loanRecordsCopy, borrowerRecordsCopy, userPrefsCopy);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentCatalog, loanRecords, borrowerRecords, userPrefs)));

        // different filteredList -> returns false
        String keywords = BOOK_1.getTitle().value;
        modelManager.updateFilteredBookList(new BookPredicate().setTitle(keywords));
        assertFalse(modelManager.equals(new ModelManager(differentCatalog, loanRecords, borrowerRecords, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredBookList(PREDICATE_SHOW_ALL_BOOKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setCatalogFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(catalog, loanRecords, borrowerRecords, differentUserPrefs)));
    }
}
