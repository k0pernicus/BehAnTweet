import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author verkyndt
 *
 */
public class TestPatternMatcher {

	public static void main(String[] args) {
		/** test classe pattern*/
		//		String entree = "abc";
		//		Pattern p = Pattern.compile("*c");
		//		Matcher m = p.matcher(entree);
		//		while (m.find())
		//		System.out.println(entree.substring(m.start(), m.end()));

		/** test avec une ligne RT enti�re BUG*/
		//		String entree = "521293952037949440;HuvenoitC;\"RT @TerrierEla: Repose en paix papy.. ? 12/10/14 ??\";Sun Oct 12 15:38:58 CEST 2014;papy";
		//		String pattern = "[\\p{Digit}]*;[[\\p{Alpha}]&&[\\p{Digit}]]*;\"RT[\\p{ASCII}]*";
		//		Pattern p = Pattern.compile(pattern);	
		//
		//		Matcher m = p.matcher(entree);
		//		while (m.find())
		//			System.out.println(entree.substring(m.start(), m.end()));

		/** test avec le debut de la ligne RT car ne fonctionne pas avec l'enti�re*/
		//		String entree = "521293952037949440;";
		//		String pattern = "[\\p{Digit}]*;";
		//		Pattern p = Pattern.compile(pattern);	
		//
		//		Matcher m = p.matcher(entree);
		//		while (m.find())
		//			System.out.println(entree.substring(m.start(), m.end()));

		/** bug resolu : dans String pattern j'avais mis && au lieu de |*/
		//		String entree = "521293952037949440;HuvenoitC;\"RT @TerrierEla: Repose en paix papy.. ? 12/10/14 ??\";Sun Oct 12 15:38:58 CEST 2014;papy";
		//		String pattern = "[\\p{Digit}]*;[[\\p{Alpha}]|[\\p{Digit}]]*;\"RT[\\p{ASCII}]*";
		//		Pattern p = Pattern.compile(pattern);	
		//
		//		Matcher m = p.matcher(entree);
		//		while (m.find())
		//			System.out.println(entree.substring(m.start(), m.end()));

		/** test du decoupage d'une ligne avec des \@*//** fonctionne pas*/
		//		String entree = "521293920743854081;teufel69;\"@y_n_kikaku @papy_ena @umb4_2 @DollsTL ?????????\";Sun Oct 12 15:38:50 CEST 2014;papy";
		//		String pattern = "( ([\\p{Digit}]*;[[\\p{Alpha}]|[\\p{Digit}]]*;)(\"@[[^;]&[\\p{ASCII}]]*;) ([\\p{ASCII}]*))";
		//		Pattern p = Pattern.compile(pattern);	
		//
		//		Matcher m = p.matcher(entree);
		//		while (m.find())
		//			System.out.println(entree.substring(m.start(), m.end()));

		/**comprend rien pour suppr les @*/
//				String entree = "521293920743854081;teufel69;\"@y_n_kikaku @papy_ena @umb4_2 @DollsTL a????????\";Sun Oct 12 15:38:50 CEST 2014;papy";
//				String pattern = "(([\\p{Digit}]*;[[\\p{Alpha}]|[\\p{Digit}]]*;)(\"[@[[^\\s]&&\\p{ASCII}]*]*)([\\p{ASCII}]*))";
//				Pattern p = Pattern.compile(pattern);	
//		
//				Matcher m = p.matcher(entree);
//				m.find();
//					System.out.println(entree.substring(m.start(), m.end()));
//		
//				System.out.println("\n\n\n\n");
//				if(m.matches()){
//					for(int i= 0; i<= m.groupCount(); ++i)
//						System.out.println("\n"+"groupe "+i+" :"+m.group(i));
//					String newline = "";
//					for(int i= 1; i<= m.groupCount(); ++i)
//						if(i!=3)
//							newline += m.group(i);
//					System.out.println("\n\n\n\n" + newline);
		//		}

		/** test suppression des @quelquechose et #quelquechose*/
		String entree = "521293920743854081;teufel69;\"#PU @y_n_kikaku @papy_ena @umb4_2 @DollsTL ??? #PU ????? #PU \";Sun Oct 12 15:38:50 CEST 2014;papy";
		String pattern = "[@|#][[^\\s]&&\\p{ASCII}]*\\s";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(entree);
		StringBuffer sb = new StringBuffer();
		while(m.find())
		   m.appendReplacement(sb, "");
		m.appendTail(sb);
		System.out.println(sb.toString());
		

	}
	/**
	 *               \"[@[[^\\p{Blank}]&&[\\p{ASCII}]]*]*
	 * "@azneefnefjn @jnjnjnjnjnjn j
	 * 
	 * */
}
