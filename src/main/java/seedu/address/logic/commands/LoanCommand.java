package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_NOT_IN_SERVE_MODE;
import static seedu.address.commons.core.Messages.MESSAGE_NO_SUCH_BOOK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.book.SerialNumber;

/**
 * Loans a Book with the given Serial Number to a Borrower
 */
public class LoanCommand extends Command {
    public static final String COMMAND_WORD = "loan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Loans a book to a borrower.\n"
            + "Command can only be used in Serve mode.\n"
            + "Parameters: " + PREFIX_SERIAL_NUMBER + "SERIAL_NUMBER\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SERIAL_NUMBER + "B00001";

    public static final String MESSAGE_SUCCESS = "Book: %1$ loaned to Borrower: %12$";

    private final SerialNumber toLoan;

    /**
     * Creates an LoanCommand to loan the specified {@code Book} to the Borrower currently served.
     *
     * @param bookSn Serial number of Book to be loaned.
     */
    public LoanCommand(SerialNumber bookSn) {
        requireNonNull(bookSn);
        this.toLoan = bookSn;
    }

    /**
     *
     * @param model {@code Model} which the command should operate on.
     * @return Feedback message of the operation result for display.
     * @throws CommandException TODO
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.isServeMode()) {
            throw new CommandException(MESSAGE_NOT_IN_SERVE_MODE);
        }

        if (!model.hasBook(this.toLoan)) {
            throw new CommandException(MESSAGE_NO_SUCH_BOOK);
        }

        // get the book
        // check if it is loaned
        // if not set the loan
        // add to loan records

        // TODO
        // check if book is currently on loaned already
        // check if currently served borrower has already loaned this book
        // model.loanBook(book);
        return null;
    }
}
