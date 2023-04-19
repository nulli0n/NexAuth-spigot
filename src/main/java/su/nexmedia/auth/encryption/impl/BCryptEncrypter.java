package su.nexmedia.auth.encryption.impl;

import org.jetbrains.annotations.NotNull;

import at.favre.lib.crypto.bcrypt.BCrypt;
import su.nexmedia.auth.api.encryption.IEncrypter;

public class BCryptEncrypter implements IEncrypter {

	@Override
	@NotNull
	public String encrypt(@NotNull String password) {
		return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
	}

	@Override
	public boolean verify(@NotNull String password, @NotNull String hash) {
		return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
	}
}
