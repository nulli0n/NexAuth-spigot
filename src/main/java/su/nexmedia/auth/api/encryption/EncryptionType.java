package su.nexmedia.auth.api.encryption;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.encryption.impl.BCryptEncrypter;
import su.nexmedia.auth.encryption.impl.DigestEncrypter;

public enum EncryptionType {

	MD5(new DigestEncrypter("MD5")),
	SHA256(new DigestEncrypter("SHA-256")),
	SHA512(new DigestEncrypter("SHA-512")),
	BCRYPT(new BCryptEncrypter()),
	;

	private final IEncrypter encrypter;

	EncryptionType(@NotNull IEncrypter encrypter) {
		this.encrypter = encrypter;
	}
	
	@NotNull
	public IEncrypter getEncrypter() {
		return this.encrypter;
	}
}
