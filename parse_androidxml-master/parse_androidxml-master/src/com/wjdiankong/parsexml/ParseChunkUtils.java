package com.wjdiankong.parsexml;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseChunkUtils {
	
	private static int stringChunkOffset = 8;
	private static int resourceChunkOffset;
	
	private static int nextChunkOffset;
	
	private static ArrayList<String> stringContentList;
	
	private static StringBuilder xmlSb = new StringBuilder();
	private static HashMap<String, String> uriPrefixMap = new HashMap<String, String>();
	private static HashMap<String, String> prefixUriMap = new HashMap<String, String>();
	
	/**
	 * ����xml��ͷ����Ϣ
	 * @param byteSrc
	 */
	public static void parseXmlHeader(byte[] byteSrc){
		byte[] xmlMagic = Utils.copyByte(byteSrc, 0, 4);
		System.out.println("magic number:"+Utils.bytesToHexString(xmlMagic));
		byte[] xmlSize = Utils.copyByte(byteSrc, 4, 4);
		System.out.println("xml size:"+Utils.bytesToHexString(xmlSize));
		
		xmlSb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlSb.append("\n");
	}
	
	/**
	 * ����StringChunk
	 * @param byteSrc
	 */
	public static void parseStringChunk(byte[] byteSrc){
		//String Chunk�ı�ʾ
		byte[] chunkTagByte = Utils.copyByte(byteSrc, stringChunkOffset, 4);
		System.out.println("string chunktag:"+Utils.bytesToHexString(chunkTagByte));
		//String Size
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, 12, 4);
		//System.out.println(Utils.bytesToHexString(chunkSizeByte));
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);
		//String Count
		byte[] chunkStringCountByte = Utils.copyByte(byteSrc, 16, 4);
		int chunkStringCount = Utils.byte2int(chunkStringCountByte);
		System.out.println("count:"+chunkStringCount);
		
		stringContentList = new ArrayList<String>(chunkStringCount);
		
		//������Ҫע����ǣ�������ĸ��ֽ���Style�����ݣ�Ȼ������ŵ��ĸ��ֽ�ʼ����0������������Ҫֱ�ӹ�����8���ֽ�
		//String Offset �����String Chunk����ʼλ��0x00000008
		byte[] chunkStringOffsetByte = Utils.copyByte(byteSrc, 28, 4);
		
		int stringContentStart = 8 + Utils.byte2int(chunkStringOffsetByte);
		System.out.println("start:"+stringContentStart);
		
		//String Content
		byte[] chunkStringContentByte = Utils.copyByte(byteSrc, stringContentStart, chunkSize);
		
		/**
		 * �ڽ����ַ�����ʱ���и����⣬���Ǳ��룺UTF-8��UTF-16,�����UTF-8�Ļ�����00��β�ģ������UTF-16�Ļ���00 00��β��
		 */
		
		/**
		 * �˴���������������AndroidManifest.xml�ļ���
		 */
		//����ĸ�ʽ�ǣ�ƫ��ֵ��ʼ�������ֽ����ַ����ĳ��ȣ��������ַ��������ݣ�������������ַ����Ľ�����00
		byte[] firstStringSizeByte = Utils.copyByte(chunkStringContentByte, 0, 2);
		//һ���ַ���Ӧ�����ֽ�
		int firstStringSize = Utils.byte2Short(firstStringSizeByte)*2;
		System.out.println("size:"+firstStringSize);
		byte[] firstStringContentByte = Utils.copyByte(chunkStringContentByte, 2, firstStringSize+2);
		String firstStringContent = new String(firstStringContentByte);
		stringContentList.add(Utils.filterStringNull(firstStringContent));
		System.out.println("first string:"+Utils.filterStringNull(firstStringContent));
		
		//���ַ������ŵ�ArrayList��
		int endStringIndex = 2+firstStringSize+2;
		while(stringContentList.size() < chunkStringCount){
			//һ���ַ���Ӧ�����ֽڣ�����Ҫ����2
			int stringSize = Utils.byte2Short(Utils.copyByte(chunkStringContentByte, endStringIndex, 2))*2;
			String str = new String(Utils.copyByte(chunkStringContentByte, endStringIndex+2, stringSize+2));
			System.out.println("str:"+Utils.filterStringNull(str));
			stringContentList.add(Utils.filterStringNull(str));
			endStringIndex += (2+stringSize+2);
		}
		
		/**
		 * �˴��Ĵ���������������Դ�ļ�xml��
		 */
		/*int stringStart = 0;
		int index = 0;
		while(index < chunkStringCount){
			byte[] stringSizeByte = Utils.copyByte(chunkStringContentByte, stringStart, 2);
			int stringSize = (stringSizeByte[1] & 0x7F);
			System.out.println("string size:"+Utils.bytesToHexString(Utils.int2Byte(stringSize)));
			if(stringSize != 0){
				//����ע����UTF-8�����
				String val = "";
				try{
					val = new String(Utils.copyByte(chunkStringContentByte, stringStart+2, stringSize), "utf-8");
				}catch(Exception e){
					System.out.println("string encode error:"+e.toString());
				}
				stringContentList.add(val);
			}else{
				stringContentList.add("");
			}
			stringStart += (stringSize+3);
			index++;
		}
		
		for(String str : stringContentList){
			System.out.println("str:"+str);
		}*/
		
		resourceChunkOffset = stringChunkOffset + Utils.byte2int(chunkSizeByte);
		
	}
	
	/**
	 * ����Resource Chunk
	 * @param byteSrc
	 */
	public static void parseResourceChunk(byte[] byteSrc){
		byte[] chunkTagByte = Utils.copyByte(byteSrc, resourceChunkOffset, 4);
		System.out.println(Utils.bytesToHexString(chunkTagByte));
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, resourceChunkOffset+4, 4);
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);
		//������Ҫע�����chunkSize�ǰ�����chunkTag��chunkSize�������ֽڵģ�������Ҫ�޳�
		byte[] resourceIdByte = Utils.copyByte(byteSrc, resourceChunkOffset+8, chunkSize-8);
		ArrayList<Integer> resourceIdList = new ArrayList<Integer>(resourceIdByte.length/4);
		for(int i=0;i<resourceIdByte.length;i+=4){
			int resId = Utils.byte2int(Utils.copyByte(resourceIdByte, i, 4));
			System.out.println("id:"+resId+",hex:"+Utils.bytesToHexString(Utils.copyByte(resourceIdByte, i, 4)));
			resourceIdList.add(resId);
		}
		
		nextChunkOffset = (resourceChunkOffset+chunkSize);
		
	}
	
	/**
	 * ����StartNamespace Chunk
	 * @param byteSrc
	 */
	public static void parseStartNamespaceChunk(byte[] byteSrc){
		//��ȡChunkTag
		byte[] chunkTagByte = Utils.copyByte(byteSrc, 0, 4);
		System.out.println(Utils.bytesToHexString(chunkTagByte));
		//��ȡChunkSize
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, 4, 4);
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);
		
		//�����к�
		byte[] lineNumberByte = Utils.copyByte(byteSrc, 8, 4);
		int lineNumber = Utils.byte2int(lineNumberByte);
		System.out.println("line number:"+lineNumber);
		
		//����prefix(������Ҫע������кź�����ĸ��ֽ�ΪFFFF,����)
		byte[] prefixByte = Utils.copyByte(byteSrc, 16, 4);
		int prefixIndex = Utils.byte2int(prefixByte);
		String prefix = stringContentList.get(prefixIndex);
		System.out.println("prefix:"+prefixIndex);
		System.out.println("prefix str:"+prefix);
		
		//����Uri
		byte[] uriByte = Utils.copyByte(byteSrc, 20, 4);
		int uriIndex = Utils.byte2int(uriByte);
		String uri = stringContentList.get(uriIndex);
		System.out.println("uri:"+uriIndex);
		System.out.println("uri str:"+uri);
		
		uriPrefixMap.put(uri, prefix);
		prefixUriMap.put(prefix, uri);
	}
	
	/**
	 * ����EndNamespace Chunk
	 * @param byteSrc
	 */
	public static void parseEndNamespaceChunk(byte[] byteSrc){
		
	}
	
	/**
	 * ����StartTag Chunk
	 * @param byteSrc
	 */
	public static void parseStartTagChunk(byte[] byteSrc){
		//����ChunkTag
		byte[] chunkTagByte = Utils.copyByte(byteSrc, 0, 4);
		System.out.println(Utils.bytesToHexString(chunkTagByte));

		//����ChunkSize
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, 4, 4);
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);

		//�����к�
		byte[] lineNumberByte = Utils.copyByte(byteSrc, 8, 4);
		int lineNumber = Utils.byte2int(lineNumberByte);
		System.out.println("line number:"+lineNumber);

		//����prefix
		byte[] prefixByte = Utils.copyByte(byteSrc, 8, 4);
		int prefixIndex = Utils.byte2int(prefixByte);
		//������ܻ᷵��-1���������-1�Ļ����Ǿ���˵û��prefix
		if(prefixIndex != -1 && prefixIndex<stringContentList.size()){
			System.out.println("prefix:"+prefixIndex);
			System.out.println("prefix str:"+stringContentList.get(prefixIndex));
		}else{
			System.out.println("prefix null");
		}

		//����Uri
		byte[] uriByte = Utils.copyByte(byteSrc, 16, 4);
		int uriIndex = Utils.byte2int(uriByte);
		if(uriIndex != -1 && prefixIndex<stringContentList.size()){
			System.out.println("uri:"+uriIndex);
			System.out.println("uri str:"+stringContentList.get(uriIndex));
		}else{
			System.out.println("uri null");
		}
		
		//����TagName
		byte[] tagNameByte = Utils.copyByte(byteSrc, 20, 4);
		System.out.println(Utils.bytesToHexString(tagNameByte));
		int tagNameIndex = Utils.byte2int(tagNameByte);
		String tagName = stringContentList.get(tagNameIndex);
		if(tagNameIndex != -1){
			System.out.println("tag name index:"+tagNameIndex);
			System.out.println("tag name str:"+tagName);
		}else{
			System.out.println("tag name null");
		}
		
		//�������Ը���(������Ҫ�����ĸ��ֽ�:14001400)
		byte[] attrCountByte = Utils.copyByte(byteSrc, 28, 4);
		int attrCount = Utils.byte2int(attrCountByte);
		System.out.println("attr count:"+attrCount);
		
		//��������
		//������Ҫע�����ÿ�����Ե�Ԫ���������Ԫ����ɣ�ÿ��Ԫ��ռ���ĸ��ֽڣ�namespaceuri, name, valuestring, type, data
		//�ڻ�ȡ��typeֵ��ʱ����Ҫ����24λ
		ArrayList<AttributeData> attrList = new ArrayList<AttributeData>(attrCount);
		for(int i=0;i<attrCount;i++){
			Integer[] values = new Integer[5];
			AttributeData attrData = new AttributeData();
			for(int j=0;j<5;j++){
				int value = Utils.byte2int(Utils.copyByte(byteSrc, 36+i*20+j*4, 4));
				switch(j){
					case 0:
						attrData.nameSpaceUri = value;
						break;
					case 1:
						attrData.name = value;
						break;
					case 2:
						attrData.valueString = value;
						break;
					case 3:
						value = (value >> 24);
						attrData.type = value;
						break;
					case 4:
						attrData.data = value;
						break;
				}
				values[j] = value;
			}
			attrList.add(attrData);
		}
		
		for(int i=0;i<attrCount;i++){
			if(attrList.get(i).nameSpaceUri != -1){
				System.out.println("nameSpaceUri:"+stringContentList.get(attrList.get(i).nameSpaceUri));
			}else{
				System.out.println("nameSpaceUri == null");
			}
			if(attrList.get(i).name != -1){
				System.out.println("name:"+stringContentList.get(attrList.get(i).name));
			}else{
				System.out.println("name == null");
			}
			if(attrList.get(i).valueString != -1){
				System.out.println("valueString:"+stringContentList.get(attrList.get(i).valueString));
			}else{
				System.out.println("valueString == null");
			}
			System.out.println("type:"+AttributeType.getAttrType(attrList.get(i).type));
			System.out.println("data:"+AttributeType.getAttributeData(attrList.get(i)));
		}
		
		//���￪ʼ����xml�ṹ
		xmlSb.append(createStartTagXml(tagName, attrList));
		
	}
	
	/**
	 * ����EndTag Chunk
	 * @param byteSrc
	 */
	public static void parseEndTagChunk(byte[] byteSrc){
		byte[] chunkTagByte = Utils.copyByte(byteSrc, 0, 4);
		System.out.println(Utils.bytesToHexString(chunkTagByte));
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, 4, 4);
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);
		
		//�����к�
		byte[] lineNumberByte = Utils.copyByte(byteSrc, 8, 4);
		int lineNumber = Utils.byte2int(lineNumberByte);
		System.out.println("line number:"+lineNumber);

		//����prefix
		byte[] prefixByte = Utils.copyByte(byteSrc, 8, 4);
		int prefixIndex = Utils.byte2int(prefixByte);
		//������ܻ᷵��-1���������-1�Ļ����Ǿ���˵û��prefix
		if(prefixIndex != -1 && prefixIndex<stringContentList.size()){
			System.out.println("prefix:"+prefixIndex);
			System.out.println("prefix str:"+stringContentList.get(prefixIndex));
		}else{
			System.out.println("prefix null");
		}

		//����Uri
		byte[] uriByte = Utils.copyByte(byteSrc, 16, 4);
		int uriIndex = Utils.byte2int(uriByte);
		if(uriIndex != -1 && prefixIndex<stringContentList.size()){
			System.out.println("uri:"+uriIndex);
			System.out.println("uri str:"+stringContentList.get(uriIndex));
		}else{
			System.out.println("uri null");
		}

		//����TagName
		byte[] tagNameByte = Utils.copyByte(byteSrc, 20, 4);
		System.out.println(Utils.bytesToHexString(tagNameByte));
		int tagNameIndex = Utils.byte2int(tagNameByte);
		String tagName = stringContentList.get(tagNameIndex);
		if(tagNameIndex != -1){
			System.out.println("tag name index:"+tagNameIndex);
			System.out.println("tag name str:"+tagName);
		}else{
			System.out.println("tag name null");
		}
		
		xmlSb.append(createEndTagXml(tagName));
	}
	
	/**
	 * ����Text Chunk
	 * @param byteSrc
	 */
	public static void parseTextChunk(byte[] byteSrc){
		byte[] chunkTagByte = Utils.copyByte(byteSrc, 0, 4);
		System.out.println(Utils.bytesToHexString(chunkTagByte));
		byte[] chunkSizeByte = Utils.copyByte(byteSrc, 4, 4);
		int chunkSize = Utils.byte2int(chunkSizeByte);
		System.out.println("chunk size:"+chunkSize);
	}
	
	/**
	 * ��ʼ����xml����������Chunk
	 * @param byteSrc
	 */
	public static void parseXmlContent(byte[] byteSrc){
		while(!isEnd(byteSrc.length)){
			byte[] chunkTagByte = Utils.copyByte(byteSrc, nextChunkOffset, 4);
			byte[] chunkSizeByte = Utils.copyByte(byteSrc, nextChunkOffset+4, 4);
			int chunkTag = Utils.byte2int(chunkTagByte);
			int chunkSize = Utils.byte2int(chunkSizeByte);
			System.out.println("chunk tag:"+Utils.bytesToHexString(chunkTagByte));
			switch(chunkTag){
				case ChunkMagicNumber.CHUNK_STARTNS:
					System.out.println("parse start namespace");
					parseStartNamespaceChunk(Utils.copyByte(byteSrc, nextChunkOffset, chunkSize));
					break;
				case ChunkMagicNumber.CHUNK_STARTTAG:
					System.out.println("parse start tag");
					parseStartTagChunk(Utils.copyByte(byteSrc, nextChunkOffset, chunkSize));
					break;
				case ChunkMagicNumber.CHUNK_ENDTAG:
					System.out.println("parse end tag");
					parseEndTagChunk(Utils.copyByte(byteSrc, nextChunkOffset, chunkSize));
					break;
				case ChunkMagicNumber.CHUNK_ENDNS:
					System.out.println("parse end namespace");
					parseEndNamespaceChunk(Utils.copyByte(byteSrc, nextChunkOffset, chunkSize));
					break;
			}
			System.out.println("+++++++++++++++++++++++++++");
			nextChunkOffset += chunkSize;
		}
		
		System.out.println("parse xml:\n"+xmlSb.toString());
		
	}
	
	/**
	 * �ѹ���õ�xmlд���ļ���
	 */
	public static void writeFormatXmlToFile(){
		FileWriter fw = null;
		try{
			fw = new FileWriter("xmltest/AndroidManifest_format.xml");
			fw.write(xmlSb.toString());
		}catch(Exception e){
			System.out.println("write format xml file error:"+e.toString());
		}finally{
			try{
				fw.close();
			}catch(Exception e){
				System.out.println("close file error:"+e.toString());
			}
		}
	}
	
	/**
	 * ����һ��xml��tag
	 * @param tagName
	 * @param attrList
	 * @return
	 */
	private static String createStartTagXml(String tagName, List<AttributeData> attrList){
		StringBuilder tagSb = new StringBuilder();
		if("manifest".equals(tagName)){
			tagSb.append("<manifest xmls:");
			StringBuilder prefixSb = new StringBuilder();
			for(String key : prefixUriMap.keySet()){
				prefixSb.append(key+":\""+prefixUriMap.get(key)+"\"");
				prefixSb.append("\n");
			}
			tagSb.append(prefixSb.toString());
		}else{
			tagSb.append("<"+tagName);
		}
		
		//��������ֵ
		if(attrList.size() == 0){
			tagSb.append(">\n");
		}else{
			tagSb.append("\n");
			for(int i=0;i<attrList.size();i++){
				AttributeData attr = attrList.get(i);
				String prefixName = uriPrefixMap.get(attr.getNameSpaceUri());
				//������Ҫע������еĵط�û��ǰ׺��
				if(prefixName == null){
					prefixName = "";
				}
				tagSb.append("    ");
				tagSb.append(prefixName+(prefixName.length() > 0 ? ":" : "")+attr.getName()+"=");
				tagSb.append("\""+AttributeType.getAttributeData(attr)+"\"");
				if(i == (attrList.size()-1)){
					tagSb.append(">");
				}
				tagSb.append("\n");
			}
		}
		
		return tagSb.toString();
	}
	
	private static String createEndTagXml(String tagName){
		return "</" + tagName + ">\n";
	}
	
	/**
	 * �ж��Ƿ��ļ�����λ����
	 * @param totalLen
	 * @return
	 */
	public static boolean isEnd(int totalLen){
		return nextChunkOffset >= totalLen;
	}
	
	public static String getStringContent(int index){
		return stringContentList.get(index);
	}
	
}
