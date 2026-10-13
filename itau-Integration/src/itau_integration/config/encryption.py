from __future__ import annotations

import base64
import hashlib
import os
from dataclasses import dataclass

from cryptography.fernet import Fernet, InvalidToken


class EncryptionConfigurationError(RuntimeError):
    """Raised when the encryption key is missing or encrypted data is invalid."""


@dataclass(frozen=True)
class EncryptionManager:
    password: str | None

    @classmethod
    def from_environment(cls) -> EncryptionManager:
        return cls(os.getenv("APP_KEY_PWD_ENCRYPT"))

    def encrypt(self, value: str | None) -> str | None:
        if value is None:
            return None
        salt = os.urandom(16)
        token = self._fernet(salt).encrypt(value.encode("utf-8"))
        return f"{salt.hex()}:{token.decode('ascii')}"

    def decrypt(self, value: str | None) -> str | None:
        if value is None:
            return None
        try:
            salt_hex, token = value.split(":", maxsplit=1)
            plaintext = self._fernet(bytes.fromhex(salt_hex)).decrypt(token.encode("ascii"))
            return plaintext.decode("utf-8")
        except (ValueError, UnicodeDecodeError, InvalidToken) as error:
            raise EncryptionConfigurationError(
                "Não foi possível descriptografar o dado; verifique APP_KEY_PWD_ENCRYPT."
            ) from error

    def _fernet(self, salt: bytes) -> Fernet:
        if not self.password:
            raise EncryptionConfigurationError(
                "A variável APP_KEY_PWD_ENCRYPT deve estar configurada."
            )
        key = hashlib.pbkdf2_hmac(
            "sha256", self.password.encode("utf-8"), salt, 390_000, dklen=32
        )
        return Fernet(base64.urlsafe_b64encode(key))
