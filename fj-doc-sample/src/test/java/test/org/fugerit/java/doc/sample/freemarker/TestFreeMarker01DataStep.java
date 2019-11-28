package test.org.fugerit.java.doc.sample.freemarker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import test.org.fugerit.java.doc.sample.model.UserModel;

public class TestFreeMarker01DataStep extends DocProcessorBasic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5507484422975101443L;

	private static final String TEST_IMG_PATH = "test/img_test_teal.png";
	
	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = CONTINUE;
		List<UserModel> userList = new ArrayList<>();
		userList.add( new UserModel( "Queen" , "Luthien", "Tinuviel" ) );
		userList.add( new UserModel( "King" , "Thorin", "Oakshield" ) );
		userList.add( new UserModel( "Strider" , "Aragorn II", null ) );
		context.setAttribute( "list", userList );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( TEST_IMG_PATH ) )  {
			String base64Test = Base64.encodeBase64String( StreamIO.readBytes( is ) );
			context.setAttribute( "testBase64Img" , base64Test );
		}
		return res;
	}

}
