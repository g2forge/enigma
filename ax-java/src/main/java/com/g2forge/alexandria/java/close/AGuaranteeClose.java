package com.g2forge.alexandria.java.close;

public abstract class AGuaranteeClose implements ICloseable {
	private transient volatile Thread thread;

	public AGuaranteeClose() {
		this(true);
	}

	public AGuaranteeClose(boolean autoclose) {
		if (autoclose) {
			this.thread = new Thread() {
				public void run() {
					thread = null;
					close();
				}
			};
			Runtime.getRuntime().addShutdownHook(thread);
		} else thread = null;
	}

	@Override
	public void close() {
		try {
			if (thread != null) Runtime.getRuntime().removeShutdownHook(thread);
		} finally {
			thread = null;
			closeInternal();
		}
	}

	protected abstract void closeInternal();
}
