class TransactionError(Exception):
    """Base exception for Itaú transaction operations."""


class TransactionValidationError(TransactionError):
    """Raised when a transaction does not satisfy the business rules."""


class TransactionNotFoundError(TransactionError):
    """Raised when a requested transaction does not exist."""


class TransactionCreateError(TransactionError):
    """Raised when a transaction could not be created."""
