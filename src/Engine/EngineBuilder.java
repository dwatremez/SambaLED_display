package Engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class EngineBuilder {

	private String codeString;
	private boolean translated = false;
	private boolean prepared = false;

	public EngineBuilder(String str)
	{
		this.codeString = str;		
	}

	public void translateIntoJava()
	{
		if(translated)
			return;
		
		int index;

		while(this.codeString.contains("#include"))
		{
			index = this.codeString.indexOf("#include");
			String importClass = this.codeString.substring(this.codeString.indexOf("<", index) + 1, this.codeString.indexOf(".", index));

			this.codeString  = this.codeString.substring(0, index) +
					"//import" + 
					this.codeString.substring(index + 8, this.codeString.indexOf(importClass, index) - 1) +
					importClass +
					this.codeString.substring(this.codeString.indexOf(importClass, index) + 3 + importClass.length());
			index += 6 + importClass.length();

			while (true)
			{
				int beginIndex = this.codeString.indexOf(importClass, index);
				if(beginIndex == -1)
					break;

				int endIndex = this.codeString.indexOf("\n", beginIndex);				
				int equalIndex = this.codeString.substring(beginIndex, endIndex).indexOf("=") + beginIndex;
				if(equalIndex != -1)
				{
					this.codeString  = this.codeString.substring(0, equalIndex + 1) +
							" new " + 
							this.codeString.substring(this.codeString.indexOf(importClass, equalIndex));		
					index = endIndex;					
				}	
			}
		}

		if(!this.codeString.contains("public static final int NEO_GRB=1") || !this.codeString.contains("public static final int NEO_KHZ800=1"))
		{
			index = 0;
			while(true)
			{
				index = this.codeString.indexOf("\n", index);
				if(this.codeString.charAt(index + 1) == '\n')
				{
					index++;
					break;
				}

				index = this.codeString.indexOf("\n", index + 1);
			}
			
			String str = "";
			if(!this.codeString.contains("public static final int NEO_GRB=1"))
				str += "\npublic static final int NEO_GRB=1;";

			if(!this.codeString.contains("public static final int NEO_KHZ800=1"))
				str += "\npublic static final int NEO_KHZ800=1;";
			
			this.codeString = this.codeString.substring(0,index) + str + "\n" + this.codeString.substring(index);				
		}


		while(this.codeString.contains("#define"))
		{
			index = this.codeString.indexOf("#define");
			this.codeString = this.codeString.substring(0, index) +
					"public static final int" + 
					this.codeString.substring(index + 7, this.codeString.indexOf(" ", index + 8)) +
					"=" +
					this.codeString.substring(this.codeString.indexOf(" ", index + 8) + 1, this.codeString.indexOf("\n", index)) +
					";" +
					this.codeString.substring(this.codeString.indexOf("\n", index));
		}

		if(this.codeString.contains("uint32_t"))
			this.codeString = this.codeString.replace("uint32_t", "int");
		
		if(this.codeString.contains("int32_t"))
			this.codeString = this.codeString.replace("int32_t", "int");

		if(this.codeString.contains("uint16_t"))
			this.codeString = this.codeString.replace("uint16_t", "int");
		
		if(this.codeString.contains("int16_t"))
			this.codeString = this.codeString.replace("int16_t", "int");

		if(this.codeString.contains("uint8_t"))
			this.codeString = this.codeString.replace("uint8_t", "int");
		
		if(this.codeString.contains("int8_t"))
			this.codeString = this.codeString.replace("int8_t", "int");

		if(this.codeString.contains("byte"))
			this.codeString = this.codeString.replace("byte", "int");
			
		this.codeString = this.codeString.replace("void setup()", "public void setup()");	
		
		translated = true;
	}

	public void prepareCode()
	{
		translateIntoJava();
		
		if(prepared)
			return;
		
		this.codeString = "package Engine;\n\npublic class InstrumentEngine implements Runnable {\n\n" + this.codeString;	
		this.codeString = this.codeString.concat("\nvoid delay(int w){try {Thread.sleep(w);} catch (InterruptedException e) {e.printStackTrace();}}");
		this.codeString = this.codeString.concat("\npublic void run(){while(true){loop();}}");
		this.codeString = this.codeString.concat("\n}");		
		
		this.prepared = true;
	}
	
	public void editEngine()
	{
		prepareCode();
		
		File f = new File("src/Engine/InstrumentEngine.java");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				f.getAbsolutePath()))) 
				{
			writer.write(codeString);
				} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
