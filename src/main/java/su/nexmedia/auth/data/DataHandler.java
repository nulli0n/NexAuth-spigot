package su.nexmedia.auth.data;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.api.encryption.EncryptionType;
import su.nexmedia.auth.data.impl.AuthUser;
import su.nexmedia.auth.data.impl.SecretKey;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.sql.SQLColumn;
import su.nexmedia.engine.api.data.sql.SQLCondition;
import su.nexmedia.engine.api.data.sql.SQLValue;
import su.nexmedia.engine.api.data.sql.column.ColumnType;
import su.nexmedia.engine.api.data.sql.executor.SelectQueryExecutor;
import su.nexmedia.engine.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<NexAuth, AuthUser> {

	private static final SQLColumn COLUMN_IP       = SQLColumn.of("ip", ColumnType.STRING);
	private static final SQLColumn COLUMN_PASSWORD = SQLColumn.of("password", ColumnType.STRING);
	private static final SQLColumn COLUMN_ENCRYPT  = SQLColumn.of("encrypt", ColumnType.STRING);
	private static final SQLColumn COLUMN_SECRET   = SQLColumn.of("secretKey", ColumnType.STRING);

	private static DataHandler instance;
	
	private final Function<ResultSet, AuthUser> userFunction;
	
	protected DataHandler(@NotNull NexAuth plugin) {
		super(plugin, plugin);
		
		this.userFunction = (resultSet) -> {
			try {
				UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
				String name = resultSet.getString(COLUMN_USER_NAME.getName());
				long dateCreate = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
				long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

				String ip = resultSet.getString(COLUMN_IP.getName());
				String password = resultSet.getString(COLUMN_PASSWORD.getName());
				EncryptionType encrypt = StringUtil.getEnum(resultSet.getString(COLUMN_ENCRYPT.getName()), EncryptionType.class).orElse(EncryptionType.MD5);
				SecretKey secretKey = this.gson.fromJson(resultSet.getString(COLUMN_SECRET.getName()), new TypeToken<SecretKey>(){}.getType());
				if (secretKey == null) secretKey = SecretKey.empty();

				return new AuthUser(plugin, uuid, name, dateCreate, lastOnline, ip, password, encrypt, secretKey);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
				return null;
			}
		};
	}

	@NotNull
	public static DataHandler getInstance(@NotNull NexAuth plugin) {
		if (instance == null) {
			instance = new DataHandler(plugin);
		}
		return instance;
	}

	@Override
	protected void onShutdown() {
		super.onShutdown();
		instance = null;
	}

	@Override
	public void onSynchronize() {

	}

	@Override
	protected void createUserTable() {
		super.createUserTable();

		this.addColumn(this.tableUsers, COLUMN_SECRET.toValue(this.gson.toJson(SecretKey.empty())));
	}

	@Override
	@NotNull
	protected List<SQLColumn> getExtraColumns() {
		return Arrays.asList(COLUMN_IP, COLUMN_PASSWORD, COLUMN_ENCRYPT, COLUMN_SECRET);
	}

	@Override
	@NotNull
	protected List<SQLValue> getSaveColumns(@NotNull AuthUser user) {
		return Arrays.asList(
			COLUMN_IP.toValue(user.getRegistrationIP()),
			COLUMN_PASSWORD.toValue(user.getHashedPassword()),
			COLUMN_ENCRYPT.toValue(user.getEncryptionType().name()),
			COLUMN_SECRET.toValue(this.gson.toJson(user.getSecretKey()))
		);
	}

	@Override
	@NotNull
	protected Function<ResultSet, AuthUser> getFunctionToUser() {
		return this.userFunction;
	}

	@Nullable
	public String getRealName(@NotNull String name) {
		Function<ResultSet, String> function = resultSet -> {
			try {
				return resultSet.getString(COLUMN_USER_NAME.getName());
			}
			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		};

		return this.load(this.tableUsers, function,
			Collections.singletonList(COLUMN_USER_NAME),
			Collections.singletonList(SQLCondition.equal(COLUMN_USER_NAME.asLowerCase().toValue(name.toLowerCase())))
		).orElse(null);
	}

    public int getUsersByIp(@NotNull String ip) {
    	AtomicInteger amount = new AtomicInteger(0);

    	Function<ResultSet, Void> function = resultSet -> {
    		amount.incrementAndGet();
    		return null;
		};

		SelectQueryExecutor.builder(this.tableUsers, function)
			.columns(COLUMN_IP, COLUMN_PASSWORD)
			.where(SQLCondition.equal(COLUMN_IP.toValue(ip)), SQLCondition.not(COLUMN_PASSWORD.toValue("''")))
			.execute(this.getConnector());
    	return amount.get();
    }
}
