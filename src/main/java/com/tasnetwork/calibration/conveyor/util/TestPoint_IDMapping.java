package com.tasnetwork.calibration.conveyor.util;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.fxml.Initializable;

public class TestPoint_IDMapping implements Initializable {

	public static JSONObject TP_ID_Map = new JSONObject();

	public static void TP_ID_MapInit() {
		try {
			TP_ID_Map = new JSONObject();
			TP_ID_Map.put("DFLT-1.0-Import", "001");
			TP_ID_Map.put("DFLT-0.5L-Import", "002");
			TP_ID_Map.put("DFLT-0.5C-Import", "003");
			TP_ID_Map.put("DFLT-0.866L-Import", "004");
			TP_ID_Map.put("DFLT-0.866C-Import", "005");
			TP_ID_Map.put("DFLT-1.0-Export", "006");
			TP_ID_Map.put("DFLT-0.5L-Export", "007");
			TP_ID_Map.put("DFLT-0.5C-Export", "008");
			TP_ID_Map.put("DFLT-0.866L-Export", "009");
			TP_ID_Map.put("DFLT-0.866C-Export", "010");
			TP_ID_Map.put("WRONG-YRB:1.0-Import", "011");
			TP_ID_Map.put("WRONG-YRB:0.5L-Import", "012");
			TP_ID_Map.put("WRONG-YRB:0.5C-Import", "013");
			TP_ID_Map.put("WRONG-YRB:0.866L-Import", "014");
			TP_ID_Map.put("WRONG-YRB:0.866C-Import", "015");
			TP_ID_Map.put("WRONG-RBY:1.0-Import", "016");
			TP_ID_Map.put("WRONG-RBY:0.5L-Import", "017");
			TP_ID_Map.put("WRONG-RBY:0.5C-Import", "018");
			TP_ID_Map.put("WRONG-RBY:0.866L-Import", "019");
			TP_ID_Map.put("WRONG-RBY:0.866C-Import", "020");
			TP_ID_Map.put("WRONG-BYR:1.0-Import", "021");
			TP_ID_Map.put("WRONG-BYR:0.5L-Import", "022");
			TP_ID_Map.put("WRONG-BYR:0.5C-Import", "023");
			TP_ID_Map.put("WRONG-BYR:0.866L-Import", "024");
			TP_ID_Map.put("WRONG-BYR:0.866C-Import", "025");
			TP_ID_Map.put("CUNB-R:1.0-Import", "026");
			TP_ID_Map.put("CUNB-R:0.5L-Import", "027");
			TP_ID_Map.put("CUNB-R:0.5C-Import", "028");
			TP_ID_Map.put("CUNB-R:0.866L-Import", "029");
			TP_ID_Map.put("CUNB-R:0.866C-Import", "030");
			TP_ID_Map.put("CUNB-Y:1.0-Import", "031");
			TP_ID_Map.put("CUNB-Y:0.5L-Import", "032");
			TP_ID_Map.put("CUNB-Y:0.5C-Import", "033");
			TP_ID_Map.put("CUNB-Y:0.866L-Import", "034");
			TP_ID_Map.put("CUNB-Y:0.866C-Import", "035");
			TP_ID_Map.put("CUNB-B:1.0-Import", "036");
			TP_ID_Map.put("CUNB-B:0.5L-Import", "037");
			TP_ID_Map.put("CUNB-B:0.5C-Import", "038");
			TP_ID_Map.put("CUNB-B:0.866L-Import", "039");
			TP_ID_Map.put("CUNB-B:0.866C-Import", "040");
			TP_ID_Map.put("CUNB-RY:1.0-Import", "041");
			TP_ID_Map.put("CUNB-RY:0.5L-Import", "042");
			TP_ID_Map.put("CUNB-RY:0.5C-Import", "043");
			TP_ID_Map.put("CUNB-RY:0.866L-Import", "044");
			TP_ID_Map.put("CUNB-RY:0.866C-Import", "045");
			TP_ID_Map.put("CUNB-RB:1.0-Import", "046");
			TP_ID_Map.put("CUNB-RB:0.5L-Import", "047");
			TP_ID_Map.put("CUNB-RB:0.5C-Import", "048");
			TP_ID_Map.put("CUNB-RB:0.866L-Import", "049");
			TP_ID_Map.put("CUNB-RB:0.866C-Import", "050");
			TP_ID_Map.put("CUNB-YB:1.0-Import", "051");
			TP_ID_Map.put("CUNB-YB:0.5L-Import", "052");
			TP_ID_Map.put("CUNB-YB:0.5C-Import", "053");
			TP_ID_Map.put("CUNB-YB:0.866L-Import", "054");
			TP_ID_Map.put("CUNB-YB:0.866C-Import", "055");
			TP_ID_Map.put("CUNB-R:1.0-Export", "056");
			TP_ID_Map.put("CUNB-R:0.5L-Export", "057");
			TP_ID_Map.put("CUNB-R:0.5C-Export", "058");
			TP_ID_Map.put("CUNB-R:0.866L-Export", "059");
			TP_ID_Map.put("CUNB-R:0.866C-Export", "060");
			TP_ID_Map.put("CUNB-Y:1.0-Export", "061");
			TP_ID_Map.put("CUNB-Y:0.5L-Export", "062");
			TP_ID_Map.put("CUNB-Y:0.5C-Export", "063");
			TP_ID_Map.put("CUNB-Y:0.866L-Export", "064");
			TP_ID_Map.put("CUNB-Y:0.866C-Export", "065");
			TP_ID_Map.put("CUNB-B:1.0-Export", "066");
			TP_ID_Map.put("CUNB-B:0.5L-Export", "067");
			TP_ID_Map.put("CUNB-B:0.5C-Export", "068");
			TP_ID_Map.put("CUNB-B:0.866L-Export", "069");
			TP_ID_Map.put("CUNB-B:0.866C-Export", "070");
			TP_ID_Map.put("CUNB-RY:1.0-Export", "071");
			TP_ID_Map.put("CUNB-RY:0.5L-Export", "072");
			TP_ID_Map.put("CUNB-RY:0.5C-Export", "073");
			TP_ID_Map.put("CUNB-RY:0.866L-Export", "074");
			TP_ID_Map.put("CUNB-RY:0.866C-Export", "075");
			TP_ID_Map.put("CUNB-RB:1.0-Export", "076");
			TP_ID_Map.put("CUNB-RB:0.5L-Export", "077");
			TP_ID_Map.put("CUNB-RB:0.5C-Export", "078");
			TP_ID_Map.put("CUNB-RB:0.866L-Export", "079");
			TP_ID_Map.put("CUNB-RB:0.866C-Export", "080");
			TP_ID_Map.put("CUNB-YB:1.0-Export", "081");
			TP_ID_Map.put("CUNB-YB:0.5L-Export", "082");
			TP_ID_Map.put("CUNB-YB:0.5C-Export", "083");
			TP_ID_Map.put("CUNB-YB:0.866L-Export", "084");
			TP_ID_Map.put("CUNB-YB:0.866C-Export", "085");
			TP_ID_Map.put("WRONG-YRB:1.0-Export", "086");
			TP_ID_Map.put("WRONG-YRB:0.5L-Export", "087");
			TP_ID_Map.put("WRONG-YRB:0.5C-Export", "088");
			TP_ID_Map.put("WRONG-YRB:0.866L-Export", "089");
			TP_ID_Map.put("WRONG-YRB:0.866C-Export", "090");
			TP_ID_Map.put("WRONG-RBY:1.0-Export", "091");
			TP_ID_Map.put("WRONG-RBY:0.5L-Export", "092");
			TP_ID_Map.put("WRONG-RBY:0.5C-Export", "093");
			TP_ID_Map.put("WRONG-RBY:0.866L-Export", "094");
			TP_ID_Map.put("WRONG-RBY:0.866C-Export", "095");
			TP_ID_Map.put("WRONG-BYR:1.0-Export", "096");
			TP_ID_Map.put("WRONG-BYR:0.5L-Export", "097");
			TP_ID_Map.put("WRONG-BYR:0.5C-Export", "098");
			TP_ID_Map.put("WRONG-BYR:0.866L-Export", "099");
			TP_ID_Map.put("WRONG-BYR:0.866C-Export", "100");
			TP_ID_Map.put("VUNB-R:1.0-Import", "101");
			TP_ID_Map.put("VUNB-R:0.5L-Import", "102");
			TP_ID_Map.put("VUNB-R:0.5C-Import", "103");
			TP_ID_Map.put("VUNB-R:0.866L-Import", "104");
			TP_ID_Map.put("VUNB-R:0.866C-Import", "105");
			TP_ID_Map.put("VUNB-Y:1.0-Import", "106");
			TP_ID_Map.put("VUNB-Y:0.5L-Import", "107");
			TP_ID_Map.put("VUNB-Y:0.5C-Import", "108");
			TP_ID_Map.put("VUNB-Y:0.866L-Import", "109");
			TP_ID_Map.put("VUNB-Y:0.866C-Import", "110");
			TP_ID_Map.put("VUNB-B:1.0-Import", "111");
			TP_ID_Map.put("VUNB-B:0.5L-Import", "112");
			TP_ID_Map.put("VUNB-B:0.5C-Import", "113");
			TP_ID_Map.put("VUNB-B:0.866L-Import", "114");
			TP_ID_Map.put("VUNB-B:0.866C-Import", "115");
			TP_ID_Map.put("VUNB-RY:1.0-Import", "116");
			TP_ID_Map.put("VUNB-RY:0.5L-Import", "117");
			TP_ID_Map.put("VUNB-RY:0.5C-Import", "118");
			TP_ID_Map.put("VUNB-RY:0.866L-Import", "119");
			TP_ID_Map.put("VUNB-RY:0.866C-Import", "120");
			TP_ID_Map.put("VUNB-RB:1.0-Import", "121");
			TP_ID_Map.put("VUNB-RB:0.5L-Import", "122");
			TP_ID_Map.put("VUNB-RB:0.5C-Import", "123");
			TP_ID_Map.put("VUNB-RB:0.866L-Import", "124");
			TP_ID_Map.put("VUNB-RB:0.866C-Import", "125");
			TP_ID_Map.put("VUNB-YB:1.0-Import", "126");
			TP_ID_Map.put("VUNB-YB:0.5L-Import", "127");
			TP_ID_Map.put("VUNB-YB:0.5C-Import", "128");
			TP_ID_Map.put("VUNB-YB:0.866L-Import", "129");
			TP_ID_Map.put("VUNB-YB:0.866C-Import", "130");
			TP_ID_Map.put("VUNB-R:1.0-Export", "131");
			TP_ID_Map.put("VUNB-R:0.5L-Export", "132");
			TP_ID_Map.put("VUNB-R:0.5C-Export", "133");
			TP_ID_Map.put("VUNB-R:0.866L-Export", "134");
			TP_ID_Map.put("VUNB-R:0.866C-Export", "135");
			TP_ID_Map.put("VUNB-Y:1.0-Export", "136");
			TP_ID_Map.put("VUNB-Y:0.5L-Export", "137");
			TP_ID_Map.put("VUNB-Y:0.5C-Export", "138");
			TP_ID_Map.put("VUNB-Y:0.866L-Export", "139");
			TP_ID_Map.put("VUNB-Y:0.866C-Export", "140");
			TP_ID_Map.put("VUNB-B:1.0-Export", "141");
			TP_ID_Map.put("VUNB-B:0.5L-Export", "142");
			TP_ID_Map.put("VUNB-B:0.5C-Export", "143");
			TP_ID_Map.put("VUNB-B:0.866L-Export", "144");
			TP_ID_Map.put("VUNB-B:0.866C-Export", "145");
			TP_ID_Map.put("VUNB-RY:1.0-Export", "146");
			TP_ID_Map.put("VUNB-RY:0.5L-Export", "147");
			TP_ID_Map.put("VUNB-RY:0.5C-Export", "148");
			TP_ID_Map.put("VUNB-RY:0.866L-Export", "149");
			TP_ID_Map.put("VUNB-RY:0.866C-Export", "150");
			TP_ID_Map.put("VUNB-RB:1.0-Export", "151");
			TP_ID_Map.put("VUNB-RB:0.5L-Export", "152");
			TP_ID_Map.put("VUNB-RB:0.5C-Export", "153");
			TP_ID_Map.put("VUNB-RB:0.866L-Export", "154");
			TP_ID_Map.put("VUNB-RB:0.866C-Export", "155");
			TP_ID_Map.put("VUNB-YB:1.0-Export", "156");
			TP_ID_Map.put("VUNB-YB:0.5L-Export", "157");
			TP_ID_Map.put("VUNB-YB:0.5C-Export", "158");
			TP_ID_Map.put("VUNB-YB:0.866L-Export", "159");
			TP_ID_Map.put("VUNB-YB:0.866C-Export", "160");
			TP_ID_Map.put("DFLT-1.0-NmleImport", "161");
			TP_ID_Map.put("DFLT-0.5L-NmleImport", "162");
			TP_ID_Map.put("DFLT-0.5C-NmleImport", "163");
			TP_ID_Map.put("DFLT-0.866L-NmleImport", "164");
			TP_ID_Map.put("DFLT-0.866C-NmleImport", "165");
			TP_ID_Map.put("DFLT-1.0-NmleExport", "166");
			TP_ID_Map.put("DFLT-0.5L-NmleExport", "167");
			TP_ID_Map.put("DFLT-0.5C-NmleExport", "168");
			TP_ID_Map.put("DFLT-0.866L-NmleExport", "169");
			TP_ID_Map.put("DFLT-0.866C-NmleExport", "170");
			TP_ID_Map.put("WRONG-YRB:1.0-NmleImport", "171");
			TP_ID_Map.put("WRONG-YRB:0.5L-NmleImport", "172");
			TP_ID_Map.put("WRONG-YRB:0.5C-NmleImport", "173");
			TP_ID_Map.put("WRONG-YRB:0.866L-NmleImport", "174");
			TP_ID_Map.put("WRONG-YRB:0.866C-NmleImport", "175");
			TP_ID_Map.put("WRONG-RBY:1.0-NmleImport", "176");
			TP_ID_Map.put("WRONG-RBY:0.5L-NmleImport", "177");
			TP_ID_Map.put("WRONG-RBY:0.5C-NmleImport", "178");
			TP_ID_Map.put("WRONG-RBY:0.866L-NmleImport", "179");
			TP_ID_Map.put("WRONG-RBY:0.866C-NmleImport", "180");
			TP_ID_Map.put("WRONG-BYR:1.0-NmleImport", "181");
			TP_ID_Map.put("WRONG-BYR:0.5L-NmleImport", "182");
			TP_ID_Map.put("WRONG-BYR:0.5C-NmleImport", "183");
			TP_ID_Map.put("WRONG-BYR:0.866L-NmleImport", "184");
			TP_ID_Map.put("WRONG-BYR:0.866C-NmleImport", "185");
			TP_ID_Map.put("CUNB-R:1.0-NmleImport", "186");
			TP_ID_Map.put("CUNB-R:0.5L-NmleImport", "187");
			TP_ID_Map.put("CUNB-R:0.5C-NmleImport", "188");
			TP_ID_Map.put("CUNB-R:0.866L-NmleImport", "189");
			TP_ID_Map.put("CUNB-R:0.866C-NmleImport", "190");
			TP_ID_Map.put("CUNB-Y:1.0-NmleImport", "191");
			TP_ID_Map.put("CUNB-Y:0.5L-NmleImport", "192");
			TP_ID_Map.put("CUNB-Y:0.5C-NmleImport", "193");
			TP_ID_Map.put("CUNB-Y:0.866L-NmleImport", "194");
			TP_ID_Map.put("CUNB-Y:0.866C-NmleImport", "195");
			TP_ID_Map.put("CUNB-B:1.0-NmleImport", "196");
			TP_ID_Map.put("CUNB-B:0.5L-NmleImport", "197");
			TP_ID_Map.put("CUNB-B:0.5C-NmleImport", "198");
			TP_ID_Map.put("CUNB-B:0.866L-NmleImport", "199");
			TP_ID_Map.put("CUNB-B:0.866C-NmleImport", "200");
			TP_ID_Map.put("CUNB-RY:1.0-NmleImport", "201");
			TP_ID_Map.put("CUNB-RY:0.5L-NmleImport", "202");
			TP_ID_Map.put("CUNB-RY:0.5C-NmleImport", "203");
			TP_ID_Map.put("CUNB-RY:0.866L-NmleImport", "204");
			TP_ID_Map.put("CUNB-RY:0.866C-NmleImport", "205");
			TP_ID_Map.put("CUNB-RB:1.0-NmleImport", "206");
			TP_ID_Map.put("CUNB-RB:0.5L-NmleImport", "207");
			TP_ID_Map.put("CUNB-RB:0.5C-NmleImport", "208");
			TP_ID_Map.put("CUNB-RB:0.866L-NmleImport", "209");
			TP_ID_Map.put("CUNB-RB:0.866C-NmleImport", "210");
			TP_ID_Map.put("CUNB-YB:1.0-NmleImport", "211");
			TP_ID_Map.put("CUNB-YB:0.5L-NmleImport", "212");
			TP_ID_Map.put("CUNB-YB:0.5C-NmleImport", "213");
			TP_ID_Map.put("CUNB-YB:0.866L-NmleImport", "214");
			TP_ID_Map.put("CUNB-YB:0.866C-NmleImport", "215");
			TP_ID_Map.put("CUNB-R:1.0-NmleExport", "216");
			TP_ID_Map.put("CUNB-R:0.5L-NmleExport", "217");
			TP_ID_Map.put("CUNB-R:0.5C-NmleExport", "218");
			TP_ID_Map.put("CUNB-R:0.866L-NmleExport", "219");
			TP_ID_Map.put("CUNB-R:0.866C-NmleExport", "220");
			TP_ID_Map.put("CUNB-Y:1.0-NmleExport", "221");
			TP_ID_Map.put("CUNB-Y:0.5L-NmleExport", "222");
			TP_ID_Map.put("CUNB-Y:0.5C-NmleExport", "223");
			TP_ID_Map.put("CUNB-Y:0.866L-NmleExport", "224");
			TP_ID_Map.put("CUNB-Y:0.866C-NmleExport", "225");
			TP_ID_Map.put("CUNB-B:1.0-NmleExport", "226");
			TP_ID_Map.put("CUNB-B:0.5L-NmleExport", "227");
			TP_ID_Map.put("CUNB-B:0.5C-NmleExport", "228");
			TP_ID_Map.put("CUNB-B:0.866L-NmleExport", "229");
			TP_ID_Map.put("CUNB-B:0.866C-NmleExport", "230");
			TP_ID_Map.put("CUNB-RY:1.0-NmleExport", "231");
			TP_ID_Map.put("CUNB-RY:0.5L-NmleExport", "232");
			TP_ID_Map.put("CUNB-RY:0.5C-NmleExport", "233");
			TP_ID_Map.put("CUNB-RY:0.866L-NmleExport", "234");
			TP_ID_Map.put("CUNB-RY:0.866C-NmleExport", "235");
			TP_ID_Map.put("CUNB-RB:1.0-NmleExport", "236");
			TP_ID_Map.put("CUNB-RB:0.5L-NmleExport", "237");
			TP_ID_Map.put("CUNB-RB:0.5C-NmleExport", "238");
			TP_ID_Map.put("CUNB-RB:0.866L-NmleExport", "239");
			TP_ID_Map.put("CUNB-RB:0.866C-NmleExport", "240");
			TP_ID_Map.put("CUNB-YB:1.0-NmleExport", "241");
			TP_ID_Map.put("CUNB-YB:0.5L-NmleExport", "242");
			TP_ID_Map.put("CUNB-YB:0.5C-NmleExport", "243");
			TP_ID_Map.put("CUNB-YB:0.866L-NmleExport", "244");
			TP_ID_Map.put("CUNB-YB:0.866C-NmleExport", "245");
			TP_ID_Map.put("WRONG-YRB:1.0-NmleExport", "246");
			TP_ID_Map.put("WRONG-YRB:0.5L-NmleExport", "247");
			TP_ID_Map.put("WRONG-YRB:0.5C-NmleExport", "248");
			TP_ID_Map.put("WRONG-YRB:0.866L-NmleExport", "249");
			TP_ID_Map.put("WRONG-YRB:0.866C-NmleExport", "250");
			TP_ID_Map.put("WRONG-RBY:1.0-NmleExport", "251");
			TP_ID_Map.put("WRONG-RBY:0.5L-NmleExport", "252");
			TP_ID_Map.put("WRONG-RBY:0.5C-NmleExport", "253");
			TP_ID_Map.put("WRONG-RBY:0.866L-NmleExport", "254");
			TP_ID_Map.put("WRONG-RBY:0.866C-NmleExport", "255");
			TP_ID_Map.put("WRONG-BYR:1.0-NmleExport", "256");
			TP_ID_Map.put("WRONG-BYR:0.5L-NmleExport", "257");
			TP_ID_Map.put("WRONG-BYR:0.5C-NmleExport", "258");
			TP_ID_Map.put("WRONG-BYR:0.866L-NmleExport", "259");
			TP_ID_Map.put("WRONG-BYR:0.866C-NmleExport", "260");
			TP_ID_Map.put("VUNB-R:1.0-NmleImport", "261");
			TP_ID_Map.put("VUNB-R:0.5L-NmleImport", "262");
			TP_ID_Map.put("VUNB-R:0.5C-NmleImport", "263");
			TP_ID_Map.put("VUNB-R:0.866L-NmleImport", "264");
			TP_ID_Map.put("VUNB-R:0.866C-NmleImport", "265");
			TP_ID_Map.put("VUNB-Y:1.0-NmleImport", "266");
			TP_ID_Map.put("VUNB-Y:0.5L-NmleImport", "267");
			TP_ID_Map.put("VUNB-Y:0.5C-NmleImport", "268");
			TP_ID_Map.put("VUNB-Y:0.866L-NmleImport", "269");
			TP_ID_Map.put("VUNB-Y:0.866C-NmleImport", "270");
			TP_ID_Map.put("VUNB-B:1.0-NmleImport", "271");
			TP_ID_Map.put("VUNB-B:0.5L-NmleImport", "272");
			TP_ID_Map.put("VUNB-B:0.5C-NmleImport", "273");
			TP_ID_Map.put("VUNB-B:0.866L-NmleImport", "274");
			TP_ID_Map.put("VUNB-B:0.866C-NmleImport", "275");
			TP_ID_Map.put("VUNB-RY:1.0-NmleImport", "276");
			TP_ID_Map.put("VUNB-RY:0.5L-NmleImport", "277");
			TP_ID_Map.put("VUNB-RY:0.5C-NmleImport", "278");
			TP_ID_Map.put("VUNB-RY:0.866L-NmleImport", "279");
			TP_ID_Map.put("VUNB-RY:0.866C-NmleImport", "280");
			TP_ID_Map.put("VUNB-RB:1.0-NmleImport", "281");
			TP_ID_Map.put("VUNB-RB:0.5L-NmleImport", "282");
			TP_ID_Map.put("VUNB-RB:0.5C-NmleImport", "283");
			TP_ID_Map.put("VUNB-RB:0.866L-NmleImport", "284");
			TP_ID_Map.put("VUNB-RB:0.866C-NmleImport", "285");
			TP_ID_Map.put("VUNB-YB:1.0-NmleImport", "286");
			TP_ID_Map.put("VUNB-YB:0.5L-NmleImport", "287");
			TP_ID_Map.put("VUNB-YB:0.5C-NmleImport", "288");
			TP_ID_Map.put("VUNB-YB:0.866L-NmleImport", "289");
			TP_ID_Map.put("VUNB-YB:0.866C-NmleImport", "290");
			TP_ID_Map.put("VUNB-R:1.0-NmleExport", "291");
			TP_ID_Map.put("VUNB-R:0.5L-NmleExport", "292");
			TP_ID_Map.put("VUNB-R:0.5C-NmleExport", "293");
			TP_ID_Map.put("VUNB-R:0.866L-NmleExport", "294");
			TP_ID_Map.put("VUNB-R:0.866C-NmleExport", "295");
			TP_ID_Map.put("VUNB-Y:1.0-NmleExport", "296");
			TP_ID_Map.put("VUNB-Y:0.5L-NmleExport", "297");
			TP_ID_Map.put("VUNB-Y:0.5C-NmleExport", "298");
			TP_ID_Map.put("VUNB-Y:0.866L-NmleExport", "299");
			TP_ID_Map.put("VUNB-Y:0.866C-NmleExport", "300");
			TP_ID_Map.put("VUNB-B:1.0-NmleExport", "301");
			TP_ID_Map.put("VUNB-B:0.5L-NmleExport", "302");
			TP_ID_Map.put("VUNB-B:0.5C-NmleExport", "303");
			TP_ID_Map.put("VUNB-B:0.866L-NmleExport", "304");
			TP_ID_Map.put("VUNB-B:0.866C-NmleExport", "305");
			TP_ID_Map.put("VUNB-RY:1.0-NmleExport", "306");
			TP_ID_Map.put("VUNB-RY:0.5L-NmleExport", "307");
			TP_ID_Map.put("VUNB-RY:0.5C-NmleExport", "308");
			TP_ID_Map.put("VUNB-RY:0.866L-NmleExport", "309");
			TP_ID_Map.put("VUNB-RY:0.866C-NmleExport", "310");
			TP_ID_Map.put("VUNB-RB:1.0-NmleExport", "311");
			TP_ID_Map.put("VUNB-RB:0.5L-NmleExport", "312");
			TP_ID_Map.put("VUNB-RB:0.5C-NmleExport", "313");
			TP_ID_Map.put("VUNB-RB:0.866L-NmleExport", "314");
			TP_ID_Map.put("VUNB-RB:0.866C-NmleExport", "315");
			TP_ID_Map.put("VUNB-YB:1.0-NmleExport", "316");
			TP_ID_Map.put("VUNB-YB:0.5L-NmleExport", "317");
			TP_ID_Map.put("VUNB-YB:0.5C-NmleExport", "318");
			TP_ID_Map.put("VUNB-YB:0.866L-NmleExport", "319");
			TP_ID_Map.put("VUNB-YB:0.866C-NmleExport", "320");

			TP_ID_Map.put("CBYPASS-R", "321");
			TP_ID_Map.put("CBYPASS-Y", "322");
			TP_ID_Map.put("CBYPASS-B", "323");
			TP_ID_Map.put("CBYPASS-RY", "324");
			TP_ID_Map.put("CBYPASS-RB", "325");
			TP_ID_Map.put("CBYPASS-YB", "326");
			TP_ID_Map.put("CBYPASS-RYB", "327");
			TP_ID_Map.put("NDIST-DiodeFwdN", "328");
			TP_ID_Map.put("NDIST-DiodeRevN", "329");
			TP_ID_Map.put("NDIST-ResNLE", "330");
			TP_ID_Map.put("NDIST-CapNLE", "331");
			TP_ID_Map.put("NDIST-ChopN", "332");

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("TP_ID_MapInit : JSONException: " + e.getMessage());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TP_ID_MapInit();

	}

	public static String getKeyTestPointName(String InputTestPointID) {

		InputTestPointID = String.format("%03d", Integer.parseInt(InputTestPointID));
		JSONObject testPointID_Map = new JSONObject();
		testPointID_Map = TP_ID_Map;
		Iterator iter = testPointID_Map.keys();
		String valueTestPointName = "";
		String keyTestPointID = null;
		while (iter.hasNext()) {
			valueTestPointName = (String) iter.next();
			keyTestPointID = null;
			try {
				keyTestPointID = testPointID_Map.getString(valueTestPointName);
				if (keyTestPointID.equals(InputTestPointID)) {
					return valueTestPointName;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

		}
		return null;
	}

	public static String getTestPoint_ID(String inputTestPoint_Name) {
		// TP_ID_MapInit();

		try {
			String AliasID_StrippedTestPoint_Name = inputTestPoint_Name.replaceAll("_.*?-", "-");
			ApplicationLauncher.logger.debug("TP_ID_MapInit : inputTestPoint_Name: " + inputTestPoint_Name);
			ApplicationLauncher.logger
					.debug("TP_ID_MapInit : AliasID_StrippedTestPoint_Name: " + AliasID_StrippedTestPoint_Name);
			if (TP_ID_Map.has(AliasID_StrippedTestPoint_Name)) {
				ApplicationLauncher.logger
						.debug("TP_ID_MapInit : TP_Name: " + TP_ID_Map.getString(AliasID_StrippedTestPoint_Name));
				return TP_ID_Map.getString(AliasID_StrippedTestPoint_Name);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getTestPoint_ID : JSONException: " + e.getMessage());
		}
		return null;
	}

}
