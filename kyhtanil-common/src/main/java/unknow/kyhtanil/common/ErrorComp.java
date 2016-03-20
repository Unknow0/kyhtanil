/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package unknow.kyhtanil.common;

import com.artemis.*;

public class ErrorComp extends PooledComponent
	{
	public static enum ErrorCode
		{
		ALREADY_LOGGED, INVALID_LOGIN, INVALID_STATE, INVALID_UUID, UNKNOWN_ERROR
		};

	public static final ErrorComp ALREADY_LOGGED=new ErrorComp(ErrorCode.ALREADY_LOGGED);
	public static final ErrorComp INVALID_LOGIN=new ErrorComp(ErrorCode.INVALID_LOGIN);
	public static final ErrorComp INVALID_STATE=new ErrorComp(ErrorCode.INVALID_STATE);
	public static final ErrorComp INVALID_UUID=new ErrorComp(ErrorCode.INVALID_UUID);
	public static final ErrorComp UNKNOWN_ERROR=new ErrorComp(ErrorCode.UNKNOWN_ERROR);

	public ErrorCode code;

	public ErrorComp()
		{
		}

	private ErrorComp(ErrorCode code)
		{
		this.code=code;
		}

	protected void reset()
		{
		}

	public String toString()
		{
		return code.toString();
		}
	}
