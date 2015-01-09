package com.ctfo.converge.core.util;

/**
 * 解析内部协议
 * 
 * @author James Date:2015-1-7 16:44:07
 * 
 */
public class ParseUtil {

	/** 是注册指令true,否则false */
	public static boolean isType36(String lineStr) {
		int a = lineStr.indexOf("{TYPE:36,");
		if(a!=-1){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) {
		String a = "CAITS 0_0 70104_13671151172 232700 U_REPT {TYPE:36,40:13,41:1100,202:2,104:黑J10300,42:70104,43:ZD-V02H,44:J103000,45:0}";
		boolean b = isType36(a);
		System.out.println(b);
	}
}
