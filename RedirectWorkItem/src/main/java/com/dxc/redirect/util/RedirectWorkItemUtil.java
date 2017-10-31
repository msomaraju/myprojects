/**
 * The MisdirectedUtil consists of utility methods
 * 
 *
 * @author  Shankar
 * @version 1.0
 *  
 */

package com.dxc.redirect.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;

import sun.misc.BASE64Encoder;

public class RedirectWorkItemUtil {

	private Properties appProps;
	private Properties configProps;

	final static Logger logger = Logger.getLogger(RedirectWorkItemUtil.class);

	public String constructImage(String[] tifs, String location)
			throws IOException {

		logger.info("In the image merge:" + location);
		int numTifs = tifs.length;
		for (int i = 0; i <= tifs.length - 1; i++) {
			File f = new File(tifs[i]);
			System.out.println("Mime Type of " + f.getName() + " is "
					+ new MimetypesFileTypeMap().getContentType(f));
		}
		BufferedImage image[] = new BufferedImage[numTifs];

		List<BufferedImage> list = new ArrayList<BufferedImage>(image.length);
		for (int i = 1; i < image.length; i++) {
			list.add(image[i]);
		}

		return location;
	}

	public String getImageFromURL(String source) throws SOAPException,
			IOException {

		logger.info("In the getImageFromURL");
		String imageLocation = configProps.getProperty("imageLocation");
		System.out.println("The Image Location" + imageLocation);
		String mergeFilePath = null;
		File filedest = null;

		// String username = "PANODEV";
		// String password = "Dev@1234";
		String userName = configProps.getProperty("rusername");
		String password = configProps.getProperty("rpassword");

		String authString = userName + ":" + password;

		String authStringEnc = new String(
				DatatypeConverter.printBase64Binary(authString.getBytes()));

		try {

			URL myurl = new URL(
					"http://10.159.37.22:9080/ImageRetrieval/AWDServer/service/services/v2/AppServer/instances/"
							+ source + "/attachments");

			URLConnection urlConnection = myurl.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);

			InputStream is = urlConnection.getInputStream();
			InputStreamReader ireader = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();

			while ((numCharsRead = ireader.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();
			System.out.println("\nXML retrieved ...\n");
			System.out.println(result);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			InputSource isource = new InputSource();
			isource.setCharacterStream(new StringReader(result));

			Document doc = builder.parse(isource);
			NodeList nodes = doc.getElementsByTagName("attachments");

			for (int ii = 0; ii < nodes.getLength(); ii++) {
				Element element = (Element) nodes.item(ii);

				NodeList name1 = element.getElementsByTagName("attachmentLink");
				Element line = (Element) name1.item(0);

				NamedNodeMap nnmap = line.getAttributes();
				Node node;

				String attrname = null;
				String attrval = null;

				if (nnmap != null) {
					// String imagebaseURL = "http://20.15.78.137:9081/";
					String imagebaseURL = "http://10.159.37.22:9080/";
					String imageSource = "";
					String imagetype = "";

					for (int x = 0; x < nnmap.getLength(); x++) {
						node = nnmap.item(x);
						attrname = node.getNodeName();
						attrval = node.getNodeValue();

						if (attrname.equalsIgnoreCase("href")) {
							String imageUrl = attrval;
							imageSource = imagebaseURL + imageUrl;
						}
						if (attrname.equalsIgnoreCase("type")) {
							imagetype = attrval;
						}
					}

					// ---- Create folder
					try {
						// Code Change
						// String destination = imageLocation + str +"/";
						String destination = imageLocation + source + "/";

						System.out.println(destination);
						File dir = new File(destination);
						System.out.println("Directory was created : "
								+ dir.mkdirs());

						// code change
						// String fileName = str + "." +
						// imagetype.substring(imagetype.lastIndexOf("/") + 1);
						String fileName = source
								+ "."
								+ imagetype.substring(imagetype
										.lastIndexOf("/") + 1);
						String destinationFile = destination + fileName;
						File f = new File(destinationFile);
						Path sources = Paths.get(destinationFile);
						Files.probeContentType(sources);
						System.out
								.println("The image Type is+++++++++++++++++++++++++ "
										+ imagetype);

						System.out.println("Mime Type of THE FILE DESTINATIOn "
								+ f.getName() + " is "
								+ new MimetypesFileTypeMap().getContentType(f));
						if (!(Files.probeContentType(sources))
								.equals("image/tiff")) {
							// Raise SOAP Exception
							throw new SOAPException(
									"Invalid File TYpe ... Request Cannot be processed...");
						}

						filedest = storeImage(imageSource, destinationFile,
								authStringEnc);

						mergeFilePath = filedest.getAbsolutePath();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return mergeFilePath;
	}

	/**
	 * getImageFromURLStream fetches the image file as stream
	 */
	public byte[] getImageFromURLStream(String sourceId) throws SOAPException {

		logger.info("In getImageFromURLStream :" + sourceId);

		byte[] fileStream = null;

		String username = configProps.getProperty("rusername");
		String password = configProps.getProperty("rpassword");
		String authString = username + ":" + password;
		String authStringEnc = new String(
				DatatypeConverter.printBase64Binary(authString.getBytes()));

		try {
			URL myurl = new URL(
					"http://10.159.37.22:9080/ImageRetrieval/AWDServer/service/services/v2/AppServer/instances/"
							+ sourceId + "/attachments");

			URLConnection urlConnection = myurl.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);

			InputStream is = urlConnection.getInputStream();
			InputStreamReader ireader = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();

			while ((numCharsRead = ireader.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			InputSource isource = new InputSource();
			isource.setCharacterStream(new StringReader(result));

			Document doc = builder.parse(isource);
			NodeList nodes = doc.getElementsByTagName("attachments");

			for (int ii = 0; ii < nodes.getLength(); ii++) {
				Element element = (Element) nodes.item(ii);

				NodeList name1 = element.getElementsByTagName("attachmentLink");
				Element line = (Element) name1.item(0);

				NamedNodeMap nnmap = line.getAttributes();
				Node node;

				String attrname = null;
				String attrval = null;

				if (nnmap != null) {
					// String imagebaseURL = "http://20.15.78.137:9081/";
					String imagebaseURL = "http://10.159.37.22:9080/";
					String imageSource = "";
					String imagetype = "";

					for (int x = 0; x < nnmap.getLength(); x++) {
						node = nnmap.item(x);
						attrname = node.getNodeName();
						attrval = node.getNodeValue();

						if (attrname.equalsIgnoreCase("href")) {
							String imageUrl = attrval;
							imageSource = imagebaseURL + imageUrl;
						}
						if (attrname.equalsIgnoreCase("type")) {
							imagetype = attrval;
						}
					}
					System.out
							.println("The value for imageType value ****************"
									+ imagetype);

					if (!(imagetype).equals("image/tiff")) {
						throw new SOAPException(
								"Invalid File TYpe ... Request Cannot be processed...");
					}
					fileStream = sendStream(imageSource, authStringEnc);

				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return fileStream;
	}

	/**
	 * Stores the image as file
	 */
	private File storeImage(String imageUrl, String destinationFile,
			String authStringEnc) throws IOException {

		logger.info("In the Store Image");

		System.out.println("Store Image in below location");
		System.out.println(imageUrl);
		System.out.println(destinationFile);
		try {
			// Connect to Image service -Start
			URL url = new URL(imageUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);
			InputStream inputImage = urlConnection.getInputStream();

			// Connect to Image service -End

			// Read inputImage and store Image in destination
			OutputStream os = new FileOutputStream(destinationFile);
			byte[] b = new byte[2048];
			int length;
			while ((length = inputImage.read(b)) != -1) {
				os.write(b, 0, length); // Write Image
			}

			// close connections
			inputImage.close();
			os.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new File(destinationFile);
	}

	/**
	 * Sends the image as stream
	 */
	public byte[] sendStream(String imageUrl, String authStringEnc)
			throws IOException {

		logger.info("In the SendStream");

		byte[] b = null;
		try {
			// Connect to Image service -Start

			URL url = new URL(imageUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);
			InputStream inputImage = urlConnection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[2048];
			while ((nRead = inputImage.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			b = buffer.toByteArray();

			inputImage.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * public Properties readProperties() throws IOException { ClassLoader
	 * classLoader = this.getClass().getClassLoader(); Properties properties =
	 * new Properties(); // properties.load(input); properties .load(new
	 * FileInputStream(
	 * "D:\\AWDExtServices\\conf\\Interface29\\interface29.properties")); return
	 * properties; }
	 */

	public byte[] mergeImages(String[] sourceId) throws IOException,
			SOAPException {
		logger.info("In Merge Images:" + sourceId.length);

		String[] mergeFile = new String[sourceId.length];

		byte[] imageBytes = null;
		String mergedFileLoc = null;
		String imagesLocation = null;
		/*
		 * String mergedFileLocations = this.readProperties().getProperty(
		 * "mergedFileLocations");
		 */
		//mergedFileLoc = this.constructImage(mergeFile,this.configProps.getProperty("mergedFile"));
		imagesLocation=configProps.getProperty("imageLocation");
		mergedFileLoc=configProps.getProperty("mergedFile");
		int image=0;
		String imagePath[]=new String[sourceId.length];
		if (sourceId.length == 1) {
			byte[] imagebyteStream = this.getImageFromURLStream(sourceId[0]);
			return imagebyteStream;

		} else {
			for (int i = 0; i < sourceId.length; i++) {
				//mergeFile[i] = this.getImageFromURL(sourceId[i]);
				byte[] imageByteArray=this.getImageFromURLStream(sourceId[i]);
				image++;
				imagePath[i]=this.storeImages(imageByteArray, imagesLocation,image);
				
			}
			
			combineImages(imagePath,mergedFileLoc);
			
			//FileInputStream fis = new FileInputStream(mergedFileLoc);
			/*File file = new File(mergedFileLoc);
			BufferedImage image = ImageIO.read(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "tiff", bos);
			imageBytes = bos.toByteArray();
			bos.close();*/
			
		}
		return imageBytes;
	}
	
	
	public String combineImages(String[] tifs, String location) throws IOException {
		
		int numTifs = tifs.length;
		
		BufferedImage image[] = new BufferedImage[numTifs];
		for (int i = 0; i < numTifs; i++) {

			SeekableStream ss = new FileSeekableStream(tifs[i]);
			ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
			PlanarImage pi = new NullOpImage(decoder.decodeAsRenderedImage(0), null, null, OpImage.OP_IO_BOUND);
			image[i] = pi.getAsBufferedImage();
			ss.close();
			
		}

		TIFFEncodeParam params = new TIFFEncodeParam();
		//.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
		OutputStream out = new FileOutputStream(location);
		ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);
		List<BufferedImage> list = new ArrayList<BufferedImage>(image.length);
		for (int i = 1; i < image.length; i++) {
			list.add(image[i]);
		}
		params.setExtraImages(list.iterator());
		encoder.encode(image[0]);
		out.close();

		System.out.println("Done.");
		return location;
	}
	
	
	
	
	
	public String storeImages(byte[] byteArray,String location,int count){
		BufferedOutputStream bos=null;
		String path="";
		try {
			 path = location+"Image"+count+".tiff";
			 File file= new File(path);
			 bos = new BufferedOutputStream(new FileOutputStream(file));
			 bos.write(byteArray);
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return path;
		
		
	}
	

	public String toDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {

			date = formatter.parse(dateInString);
			System.out.println(date);
			System.out.println(formatter.format(date));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.toString();
	}

	public String createCase(String targetMetsystem, String userName,
			String password, String transRefGuid, String receivedDate,
			String encodedImage) throws SAXException, IOException,
			ParserConfigurationException, TransformerException {
		
		logger.info("In createCase transRefGuid:"+transRefGuid);
		String filePath = appProps.getProperty("EXTERNAL_PATH") + "request.xml";
		logger.info("In createCase filePath:"+filePath);

		File file = new File(filePath);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		Document doc = builder.parse(file);

		Node filedValue = doc.getElementsByTagName("urn:FieldValue").item(1);

		filedValue.setTextContent(targetMetsystem);

		Node user = doc.getElementsByTagName("oas:Username").item(0);
		user.setTextContent(userName);

		Node pass = doc.getElementsByTagName("oas:Password").item(0);
		pass.setTextContent(password);

		Node tranRef = doc.getElementsByTagName("cre:TransRefGUID").item(0);
		tranRef.setTextContent(transRefGuid);

		Node attachment = doc
				.getElementsByTagName("cre:AttachmentData64Binary").item(0);
		attachment.setTextContent(encodedImage);

		Node date = doc.getElementsByTagName("cre:ReceivedDate").item(0);
		date.setTextContent(receivedDate);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY_MM_dd_hh_mm_ss");  
		
		String modifiedDate = formatter.format(new Date());
		

		//String modifiedPath = appProps.getProperty("EXTERNAL_PATH")	+ "modified.xml";
		String modifiedPath = appProps.getProperty("EXTERNAL_PATH")	+ modifiedDate+".xml";

		StreamResult result = new StreamResult(modifiedPath);
		transformer.transform(source, result);
		logger.info("request.xml updated:");
		String requestString = this.readModifiedRequestFile(modifiedPath);
		//this.deleteModifiedXmlFile(modifiedPath);

		return requestString;

	}

	public Properties getConfigProps() {
		return configProps;
	}

	public void setConfigProps(Properties configProps) {
		this.configProps = configProps;
	}

	public void deleteModifiedXmlFile(String filePath) {
		
		File file = new File(filePath);
		file.delete();
		logger.info("Deleted modified.xml file");
		

	}

	public String readModifiedRequestFile(String filePath) {

		FileReader fileR = null;
		BufferedReader reader = null;
		String str = "";
		StringBuffer message = new StringBuffer();

		try {
			fileR = new FileReader(filePath);
			reader = new BufferedReader(fileR);
			while ((str = reader.readLine()) != null) {
				message.append(str);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Modified Xml File :"+filePath);
		return message.toString();

	}

	public String encodeImage(byte[] imageBytes) throws IOException {
		logger.info("Start Image Encoding");
		String imageString = "";
		imageString=Base64.encodeBase64String(imageBytes);
		//BASE64Encoder encoder = new BASE64Encoder();
		//imageString = encoder.encode(imageBytes);
		logger.info("End Image Encoding");
		return imageString;

	}

	public String sendRequest(String requestMessage) throws SOAPException,
			IOException {

		
		URL endPoint = new URL(configProps.getProperty("endPoint"));
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage request = factory.createMessage(null,
				new ByteArrayInputStream(requestMessage.getBytes()));
		SOAPConnection connection = SOAPConnectionFactory.newInstance()
				.createConnection();
		SOAPMessage response = connection.call(request, endPoint);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		response.writeTo(out);
		return out.toString();

	}

	public void loadProperties() {
		appProps = new Properties();
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream inputStream = loader
				.getResourceAsStream("appConfig.properties");
		String path = "";
		try {
			appProps.load(inputStream);
			path = appProps.getProperty("EXTERNAL_PATH");
			String externalPath = path + "interface29.properties";
			configProps = new Properties();
			InputStream extPathStream = new FileInputStream(externalPath);
			configProps.load(extPathStream);
			PropertyConfigurator.configure(path + "log4j.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
