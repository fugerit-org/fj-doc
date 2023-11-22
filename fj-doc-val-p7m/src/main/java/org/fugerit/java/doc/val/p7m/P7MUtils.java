package org.fugerit.java.doc.val.p7m;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.fugerit.java.core.io.StreamIO;

public class P7MUtils {

	private P7MUtils() {}
	
	public static void extractContent( InputStream p7mContent, OutputStream contentStream ) throws CMSException, IOException  {
		CMSSignedData csd = new CMSSignedData( p7mContent );
		CMSProcessable cmsContent = csd.getSignedContent();
		if ( cmsContent != null ) {
			byte[] content = (byte[])cmsContent.getContent();
			try ( ByteArrayInputStream is = new ByteArrayInputStream( content ) ) {
				StreamIO.pipeStream(is, contentStream, StreamIO.MODE_CLOSE_BOTH);
			}
		}
	}
	
}
