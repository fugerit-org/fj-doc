package org.fugerit.java.doc.val.p7m;

import java.io.*;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.openssl.PEMParser;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.StreamIO;

@Slf4j
public class P7MUtils {

	private P7MUtils() {}

	public static void checkContentInfo(ContentInfo ci) throws CMSException {
		if ( ci == null ) {
			throw new CMSException( "null ContentInfo" );
		} else if (!ci.getContentType().equals(CMSObjectIdentifiers.signedData)) {
			throw new CMSException("not SignedData");
		}
	}

	public static void extractContentPEMParser( byte[] data, OutputStream contentStream ) throws CMSException, IOException  {
		String in = new String(data);
		try ( PEMParser pp = new PEMParser(new StringReader(in) ) ) {
			ContentInfo ci = (ContentInfo) pp.readObject();
			checkContentInfo( ci );	// check ContentInfo integrity
			SignedData sd = SignedData.getInstance(ci.getContent());
			byte[] encoded = ((ASN1Sequence) sd.getCertificates().getObjects().nextElement()).getEncoded();
			log.info( "validation ok! :{}", encoded.length );
			contentStream.write( encoded );
		}
	}

	public static void extractContentCMSSignedDataProcess(CMSProcessable cmsContent, OutputStream contentStream) throws CMSException, IOException {
		if ( cmsContent != null ) {
			byte[] content = (byte[])cmsContent.getContent();
			try ( ByteArrayInputStream is = new ByteArrayInputStream( content ) ) {
				StreamIO.pipeStream(is, contentStream, StreamIO.MODE_CLOSE_BOTH);
			}
		} else {
			throw new CMSException( "null CMSProcessable" );
		}
	}

	public static void extractContentCMSSignedData( byte[] data, OutputStream contentStream ) throws CMSException, IOException  {
		CMSSignedData csd = new CMSSignedData( data );
		CMSProcessable cmsContent = csd.getSignedContent();
		extractContentCMSSignedDataProcess( cmsContent, contentStream );
	}

	public static void extractContent( InputStream p7mContent, OutputStream contentStream ) throws CMSException, IOException  {
		byte[] data = StreamIO.readBytes( p7mContent );
		try {
			extractContentCMSSignedData( data, contentStream );
		} catch (CMSException e) {
			log.warn("failed extractContentCMSSignedData(), try PEMParser(), size:{} (error:{})", data.length, e.toString() );
			extractContentPEMParser( data, contentStream );
		}
	}

	public static byte[] extractContent( InputStream p7mContent ) throws CMSException, IOException  {
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			extractContent( p7mContent, buffer );
			buffer.flush();
			return buffer.toByteArray();
		}
	}
	
}
