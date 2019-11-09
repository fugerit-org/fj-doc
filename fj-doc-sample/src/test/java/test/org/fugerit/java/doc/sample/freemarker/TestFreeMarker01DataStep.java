package test.org.fugerit.java.doc.sample.freemarker;

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import test.org.fugerit.java.doc.sample.model.UserModel;

public class TestFreeMarker01DataStep extends DocProcessorBasic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5507484422975101443L;

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		int res = CONTINUE;
		List<UserModel> userList = new ArrayList<>();
		userList.add( new UserModel( "Queen" , "Luthien", "Tinuviel" ) );
		userList.add( new UserModel( "King" , "Thorin", "Oakshield" ) );
		userList.add( new UserModel( "Strider" , "Aragorn II", null ) );
		context.setAttribute( "list", userList );
		return res;
	}

}
