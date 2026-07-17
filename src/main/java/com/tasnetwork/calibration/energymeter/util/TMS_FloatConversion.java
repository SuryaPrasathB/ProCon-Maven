package com.tasnetwork.calibration.energymeter.util;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class TMS_FloatConversion {
	
	

	public static String hextofloat(String  hexNumber){

		double output=0.0f;
		try{
			int raw = Integer.parseUnsignedInt(hexNumber, 16);
			int s = (raw >> 23) & 1;
				//ApplicationLauncher.logger.info("s:"+s);
			int mantissa = (raw & 0x007fffff);
				//ApplicationLauncher.logger.info("mantissa:"+mantissa);
			int exponent = raw >> 24;
				//ApplicationLauncher.logger.info("exponent1:"+exponent);
			if (exponent >= 128){
				exponent -= 256;
				//ApplicationLauncher.logger.info("exponent2:"+exponent);
			}
			if (exponent == -128){
				output= 0.0f;
				//ApplicationLauncher.logger.info("exponent3:"+exponent);
			}
			else{
				//ApplicationLauncher.logger.info("output Value1:"+((-2) ^ s));
				/*		ApplicationLauncher.logger.info("output Value1:"+(Math.pow(-2, s)));
		ApplicationLauncher.logger.info("output Value2:"+( (float)(mantissa)));
		ApplicationLauncher.logger.info("output Value3:"+((float)(1 << 23)));
		ApplicationLauncher.logger.info("output Value4:"+( ( (float)(mantissa) / (float)(1 << 23))));
		ApplicationLauncher.logger.info("output Value5:"+(((Math.pow(-2, s)) + (float)(mantissa) / (float)(1 << 23)))   );
		ApplicationLauncher.logger.info("output Value6:"+(Math.pow(2, exponent)));*/
				//output= (((-2) ^ s) + ((float)(mantissa)) / ((float)(1 << 23))) * (2 ^ exponent);
				output= ((Math.pow(-2, s)) + ((float)(mantissa)) / ((float)(1 << 23))) * (Math.pow(2, exponent));
			}
		} catch (Exception e){
			//System.out.printf("Exception1 on hextofloat:", e.toString());
			e.printStackTrace();
			ApplicationLauncher.logger.error("hextofloat: Exception1:" +e.getMessage());
		}

		//ApplicationLauncher.logger.info(output);
		//ApplicationLauncher.logger.info("output:"+ output);
		String Data ="";
		try{
			Data =String.format("%.4f",output);
		} catch (Exception e){

			e.printStackTrace();
			ApplicationLauncher.logger.error("hextofloat: Exception2:" +e.getMessage());
			
		}
		return Data;
		//System.out.printf("%f", f);
	}	
    public static String FloatToTMS320_Hex(double floatIn){
    	String OutPutHex = "";
    	int OutPutData=0;
    	float mantissa =0;
    	int exponent =0;
    	Boolean sign = false;
    	
    	mantissa = getMantissa(floatIn);
    	exponent = Math.getExponent(floatIn);
    	sign = (Math.copySign(1.0, floatIn) < 0);
    	ApplicationLauncher.logger.debug("FloatToTMS320_Hex: mantissa:" +mantissa);
    	ApplicationLauncher.logger.debug("FloatToTMS320_Hex: exponent:" +exponent);
    	ApplicationLauncher.logger.debug("FloatToTMS320_Hex: sign:" +sign);
        if (floatIn == 0.0){
        	if(sign){
        		//ApplicationLauncher.logger.debug("test1");
        		OutPutData =  (128 << 24)| (1 << 23) ;
        	//OutPutData = -OutPutData;
        	}else{
        		//ApplicationLauncher.logger.debug("test2");
        		OutPutData =  (128 << 24) | (0 << 23);
        	}
        	//OutPutData = 2147483648;
        	OutPutHex = String.format("%08X", OutPutData);
        	//OutPutHex = "80000000";
        	ApplicationLauncher.logger.debug("FloatToTMS320_Hex1: OutPutData:" +OutPutHex);
        	return OutPutHex;
        	//return (128 << 24) | (sign << 23);
        	
        }
        
        if (sign){
        	mantissa += 2.0f;
            if( mantissa == 1.0){
            	mantissa = 0.0f;
            	exponent -= 1;
            }
        } else{
        	mantissa -= 1.0f;
        } 
    	ApplicationLauncher.logger.debug("FloatToTMS320_Hex: mantissa3:" +mantissa);
    	ApplicationLauncher.logger.debug("FloatToTMS320_Hex: exponent3:" +exponent);
        if(sign){
        	OutPutData =((exponent & 0xff) << 24) | (1 << 23)  | (int)(mantissa * (1 << 23) + 0.5);
        } else {
        	OutPutData =((exponent & 0xff) << 24) | (0 << 23) | (int)(mantissa * (1 << 23) + 0.5);
        }
        OutPutHex = String.format("%08X", OutPutData);
        ApplicationLauncher.logger.debug("FloatToTMS320_Hex-L: OutPutData:" +OutPutData);
        ApplicationLauncher.logger.debug("FloatToTMS320_Hex-L: OutPutHex:" +OutPutHex);
    	return OutPutHex;
    	
    }
    
    public static float getMantissa(double x) {
        int exponent = Math.getExponent(x);
        return  (float)(x / Math.pow(2, exponent));
    }
	
	/********************************* orginal python script for hextofloat conversion********************
https://stackoverflow.com/questions/10694352/hex-decimal-to-float-python
def tms320_float(raw):
    print("raw:"+str(raw))
    s = (raw >> 23) & 1
    print("s:"+str(s))
    mantissa = (raw & 0x007fffff)
    print("mantissa:"+str(mantissa))
    exponent = raw >> 24
    print("exponent1:"+str(exponent))
    if exponent >= 128:
        exponent -= 256
        print("exponent2:"+str(exponent))
    if exponent == -128:
        print("exponent3:"+str(exponent))
        return 0.0
    print("output Value1:"+str((-2) ** s))
    print("output Value2:"+str( float(mantissa)))
    print("output Value3:"+str(float(1 << 23)))
    print("output Value4:"+str( ( float(mantissa) / float(1 << 23))))
    print("output Value5:"+str((((-2) ** s) + float(mantissa) / float(1 << 23)))   )
    print("output Value6:"+str(2.0 ** exponent))
    return (((-2) ** s) + float(mantissa) / float(1 << 23)) * (2.0 ** exponent)


Value =tms320_float(0x076616A3)
print("volt Value:"+str(Value))
Value =tms320_float(0x021FF876)
print("Current Value:"+str(Value))
Value =tms320_float(0x0A0FC76C)
print("Watt Value:"+str(Value))
Value =tms320_float(0x0A0FC75B)
print("VA Value:"+str(Value))
Value =tms320_float(0x0547FF28)
print("Freq Value:"+str(Value))
Value =tms320_float(0x00000000)
print("Value:"+str(Value))
**********************output*******************************
*
*>>> 
raw:124130979
s:0
mantissa:6690467
exponent1:7
output Value1:1
output Value2:6690467.0
output Value3:8388608.0
output Value4:0.7975658178329468
output Value5:1.7975658178329468
output Value6:128.0
volt Value:230.0884246826172
raw:35649654
s:0
mantissa:2095222
exponent1:2
output Value1:1
output Value2:2095222.0
output Value3:8388608.0
output Value4:0.249769926071167
output Value5:1.249769926071167
output Value6:4.0
Current Value:4.999079704284668
raw:168806252
s:0
mantissa:1034092
exponent1:10
output Value1:1
output Value2:1034092.0
output Value3:8388608.0
output Value4:0.12327337265014648
output Value5:1.1232733726501465
output Value6:1024.0
Watt Value:1150.23193359375
raw:168806235
s:0
mantissa:1034075
exponent1:10
output Value1:1
output Value2:1034075.0
output Value3:8388608.0
output Value4:0.12327134609222412
output Value5:1.1232713460922241
output Value6:1024.0
VA Value:1150.2298583984375
raw:88604456
s:0
mantissa:4718376
exponent1:5
output Value1:1
output Value2:4718376.0
output Value3:8388608.0
output Value4:0.562474250793457
output Value5:1.562474250793457
output Value6:32.0
Freq Value:49.999176025390625
raw:0
s:0
mantissa:0
exponent1:0
output Value1:1
output Value2:0.0
output Value3:8388608.0
output Value4:0.0
output Value5:1.0
output Value6:1.0
Value:1.0
	 * 
	 */
/*
 * Orgignal python script for float to TMS Hex*****************************
 * 
 * import math

def float_to_ti( f ):
    m, e = math.frexp(f)
    print ('m:'+m)
    print ('e:'+str(e))
    m *= 2
    e -= 1
    print ('m1:'+str(m))
    print ('e2:'+str(e))
    sign = (math.copysign(1.0, f) < 0)
    print ('sign:'+str(sign))
    
    if f == 0.0:
        value1=(128 << 24) | (sign << 23)
        print ('value1:'+str(value1))
        return value1
    if sign:
        m += 2.0
        if m == 1.0:
            m = 0.0
            e -= 1
    else:
        m -= 1.0
    print ('m3:'+str(m))
    print ('e3:'+str(e))
    assert 0.0 <= m < 1.0
    return ((e & 0xff) << 24) | (sign << 23) | int(m * (1 << 23) + 0.5)

##floatValue = float_to_ti( 0.0 )
##HexValue = hex(floatValue)
##print ('HexValue0:'+str(HexValue))
##if(str(floatValue) == "124130979"):
##    print ('Test0 passed :floatValue:'+str(floatValue))
##else:
##    print ('Test0 failed :floatValue:'+str(floatValue))

floatValue = float_to_ti( 230.0884246826172 )
if(str(floatValue) == "124130979"):
    print ('Test1 passed :floatValue:'+str(floatValue))
else:
    print ('Test1 failed :floatValue:'+str(floatValue))

##floatValue = float_to_ti( 4.999079704284668 )
##if(str(floatValue) == "35649654"):
##    print ('Test2 passed :floatValue:'+str(floatValue))
##else:
##    print ('Test2 failed :floatValue:'+str(floatValue))
##
##floatValue = float_to_ti( 1150.23193359375 )
##if(str(floatValue) == "168806252"):
##    print ('Test3 passed :floatValue:'+str(floatValue))
##else:
##    print ('Test3 failed :floatValue:'+str(floatValue))
##
##
##floatValue = float_to_ti( 49.999176025390625 )
##if(str(floatValue) == "88604456"):
##    print ('Test4 passed :floatValue:'+str(floatValue))
##else:
##    print ('Test4 failed :floatValue:'+str(floatValue))
##
##floatValue = float_to_ti( 1.0)
##if(str(floatValue) == "0"):
##    print ('Test5 passed :floatValue:'+str(floatValue))
##else:
##    print ('Test5 failed :floatValue:'+str(floatValue))

 * 		FloatToTMS320_Hex(0.0);//(230.0884246826172);
		FloatToTMS320_Hex(230.0884246826172);
		FloatToTMS320_Hex(4.999079704284668);
		FloatToTMS320_Hex(1150.23193359375);
		FloatToTMS320_Hex(1150.2298583984375);
		FloatToTMS320_Hex(49.999176025390625);
		FloatToTMS320_Hex(1.0);
		FloatToTMS320_Hex(0.000010000);
		FloatToTMS320_Hex(0.000010001);
		FloatToTMS320_Hex(0.000010002);	
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:0.0
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:-1023
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:305 - FloatToTMS320_Hex1: OutPutData:80000000
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.7975658
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:7
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.7975658
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:7
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:124130979
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:076616A3
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.2497699
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:2
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.24976993
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:2
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:35649654
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:021FF876
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.1232734
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:10
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.12327337
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:10
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:168806252
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:0A0FC76C
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.1232713
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:10
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.123271346
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:10
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:168806235
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:0A0FC75B
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.5624743
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:5
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.56247425
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:5
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:88604456
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:0547FF28
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.0
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:0
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.0
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:0
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:0
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:00000000
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.31072
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:-17
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.31071997
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:-17
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:-282606164
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:EF27C5AC
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.3108511
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:-17
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.3108511
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:-17
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:-282605064
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:EF27C9F8
	2018-06-20 06:12:58 DEBUG ProCAL:290 - FloatToTMS320_Hex: mantissa:1.3109821
	2018-06-20 06:12:58 DEBUG ProCAL:291 - FloatToTMS320_Hex: exponent:-17
	2018-06-20 06:12:58 DEBUG ProCAL:292 - FloatToTMS320_Hex: sign:false
	2018-06-20 06:12:58 DEBUG ProCAL:320 - FloatToTMS320_Hex: mantissa3:0.3109821
	2018-06-20 06:12:58 DEBUG ProCAL:321 - FloatToTMS320_Hex: exponent3:-17
	2018-06-20 06:12:58 DEBUG ProCAL:328 - FloatToTMS320_Hex-L: OutPutData:-282603965
	2018-06-20 06:12:58 DEBUG ProCAL:329 - FloatToTMS320_Hex-L: OutPutHex:EF27CE43
	
	*/
	
}
