package edu.uiowa.slis.DocumentContentExtractor;

/**
 * Hello world!
 *
 */
public class App 
{
//	Uncomment and change for local files
//	private static String urlString = "/Users/hpitawela/Downloads/C5RA12901J.pdf"; 
	
//	Uncomment for pdf
//	private static String urlString = "https://www.ctsi.ucla.edu/researcher-resources/files/view/docs/DropIn_Statistical_Consults.pdf";
	
//	Uncomment for ppt
//	private static String urlString = " http://www.itmat.upenn.edu/assets/user-content//documents/grant_writting_ITMAT_symposium.ppt";

//	Uncomment for docx
	private static String urlString = "https://www.ctsi.ufl.edu/files/2018/06/CTSI-Webpage-IND-Final-Report.docx";

	public static void main(String[] args) {
		new DocumentContentExtractor(urlString, false).printContent();;

	}
}
