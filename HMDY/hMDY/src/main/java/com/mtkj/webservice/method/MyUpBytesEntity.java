package com.mtkj.webservice.method;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.ByteArrayEntity;

import com.mtkj.webservice.method.MyUpFileEntity.ProgressListener;
//MultipartEntity  
public class MyUpBytesEntity extends ByteArrayEntity {

	private final ProgressListener listener;

	public MyUpBytesEntity(byte[] b, final ProgressListener listener) {
		super(b);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}