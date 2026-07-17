CREATE DATABASE  IF NOT EXISTS `procal_spring_v1_0_3` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `procal_spring_v1_0_3`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: procal_spring_v1_0_3
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `rp_manage`
--

DROP TABLE IF EXISTS `rp_manage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_manage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active_profile` varchar(3) NOT NULL,
  `append_dut_serial_no_and_rack_position` varchar(3) NOT NULL,
  `base_template_meta_data_populate_type` varchar(45) DEFAULT NULL,
  `base_template_name` varchar(45) DEFAULT NULL,
  `base_template_test_filter_data_populate_type` varchar(45) DEFAULT NULL,
  `customer_id` varchar(45) DEFAULT NULL,
  `dut_main_ct_selected` varchar(3) NOT NULL,
  `dut_meta_data_apply_for_all_pages` varchar(3) NOT NULL,
  `dut_meta_data_club_page_no_and_max_no_of_pages` varchar(3) NOT NULL,
  `dut_neutral_ct_selected` varchar(3) NOT NULL,
  `export_mode_selected` varchar(3) NOT NULL,
  `import_mode_selected` varchar(3) NOT NULL,
  `max_dut_display_per_page` int(11) DEFAULT NULL,
  `output_folder_path` varchar(500) DEFAULT NULL,
  `parameter_profile_name` varchar(100) DEFAULT NULL,
  `report_group_id` varchar(45) DEFAULT NULL,
  `report_group_name` varchar(45) DEFAULT NULL,
  `report_profile_id` varchar(45) DEFAULT NULL,
  `report_profile_name` varchar(45) DEFAULT NULL,
  `split_dut_display_in_to_multiple_page` varchar(3) NOT NULL,
  `template_file_name_with_path` varchar(500) DEFAULT NULL,
  `print_style_generic_header` varchar(100) DEFAULT NULL,
  `print_style_result` varchar(100) DEFAULT NULL,
  `print_style_table_header` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_manage`
--

LOCK TABLES `rp_manage` WRITE;
/*!40000 ALTER TABLE `rp_manage` DISABLE KEYS */;
INSERT INTO `rp_manage` VALUES (1,'Y','N','Vertical','Base Template1','Vertical','2','Y','N','N','N','N','Y',20,'C:\\TAS_Network\\Procal_Excel\\Output\\','ParamProfile1','','ReportGroup1','','ReportProfile1','N','C:\\TAS_Network\\Procal_Excel\\ReportTemplate\\LecsHealthyMeterReport-V1_0.xlsx','GenericHeaderStyle','ResultStyle','TableHeaderStyle');
/*!40000 ALTER TABLE `rp_manage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rp_meter_meta_data_filter`
--

DROP TABLE IF EXISTS `rp_meter_meta_data_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_meter_meta_data_filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active_filter` varchar(3) NOT NULL,
  `base_template_meta_data_populate_type` varchar(45) DEFAULT NULL,
  `cell_position` varchar(45) DEFAULT NULL,
  `discard_rack_position_in_dut_serial_number` varchar(3) NOT NULL,
  `filter_name` varchar(45) DEFAULT NULL,
  `meter_data_type` varchar(45) DEFAULT NULL,
  `page_number` int(11) DEFAULT NULL,
  `populate_for_each_dut` varchar(3) NOT NULL,
  `populate_only_on_header` varchar(3) NOT NULL,
  `table_serial_no` varchar(45) DEFAULT NULL,
  `rp_manage` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK23241F8AA4C2B18F` (`rp_manage`),
  CONSTRAINT `FK23241F8AA4C2B18F` FOREIGN KEY (`rp_manage`) REFERENCES `rp_manage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_meter_meta_data_filter`
--

LOCK TABLES `rp_meter_meta_data_filter` WRITE;
/*!40000 ALTER TABLE `rp_meter_meta_data_filter` DISABLE KEYS */;
INSERT INTO `rp_meter_meta_data_filter` VALUES (348,'Y','Vertical','B8','N','Page1','Serial No',1,'Y','N','1',1),(349,'Y','Vertical','C8','N','Page1','Meter Serial No',1,'Y','N','2',1),(350,'Y','Vertical','L3','N','Page1','Meter Type',1,'N','Y','3',1),(351,'Y','Vertical','Q8','N','Page1','MUT Page Status',1,'Y','N','4',1),(352,'Y','Vertical','R8','N','Page1','MUT OverAll Status',1,'Y','N','5',1),(353,'Y','Vertical','S3','N','Page1','Meter Class',1,'N','Y','6',1),(354,'Y','Vertical','F30','N','Page1','Execution Date',1,'N','Y','7',1),(355,'Y','Vertical','B48','Y','Page1','Serial No',2,'Y','N','8',1),(356,'Y','Vertical','C48','Y','Page1','Meter Serial No',2,'Y','N','9',1),(357,'Y','Vertical','E48','Y','Page1','Capacity',2,'Y','N','10',1),(358,'Y','Vertical','D48','Y','Page1','Meter Constant',2,'Y','N','11',1),(359,'Y','Vertical','F48','Y','Page1','PT ratio',2,'Y','N','12',1),(360,'Y','Vertical','G48','Y','Page1','CT ratio',2,'Y','N','13',1),(361,'Y','Vertical','Q48','Y','Page1','MUT Page Status',2,'Y','N','14',1),(362,'Y','Vertical','H72','Y','Page1','Execution Time Stamp',2,'N','Y','15',1),(363,'Y','Vertical','A72','Y','Page1','Execution Date',2,'N','Y','16',1),(364,'Y','Vertical','E72','Y','Page1','Execution Time',2,'N','Y','17',1),(365,'Y','Vertical','P3','Y','Page1','Active/Reactive Energy',2,'N','Y','18',1),(366,'Y','Vertical','F29','Y','Page1','Complies/Does not comply',2,'N','Y','19',1);
/*!40000 ALTER TABLE `rp_meter_meta_data_filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rp_operation_param_profile`
--

DROP TABLE IF EXISTS `rp_operation_param_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_operation_param_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(45) DEFAULT NULL,
  `key_param` varchar(45) DEFAULT NULL,
  `operation_param_profile_name` varchar(45) DEFAULT NULL,
  `param_type` varchar(45) DEFAULT NULL,
  `populate_all_dut` varchar(3) NOT NULL,
  `populate_only_headers` varchar(3) NOT NULL,
  `table_serial_no` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_operation_param_profile`
--

LOCK TABLES `rp_operation_param_profile` WRITE;
/*!40000 ALTER TABLE `rp_operation_param_profile` DISABLE KEYS */;
INSERT INTO `rp_operation_param_profile` VALUES (3,'2','Page2_InputConstDutInitial','ParamProfile1','PageInput','Y','N','1'),(4,'2','Page2_InputConstDutFinal','ParamProfile1','PageInput','Y','N','2'),(7,'2','Page1_InputDataSet05','ParamProfile1','PageInput','Y','N','3'),(8,'2','Page1_InputDataSet06','ParamProfile1','PageInput','Y','N','4'),(9,'2','Page1_InputDataSet07','ParamProfile1','PageInput','Y','N','5'),(10,'2','Page1_InputDataSet08','ParamProfile1','PageInput','Y','N','6'),(11,'2','Page1_InputDataSet09','ParamProfile1','PageInput','Y','N','7'),(12,'2','Page2_InputConstPulseCnt','ParamProfile1','PageInput','Y','N','8'),(14,'2','Page1_PrintDataRepAvg','ParamProfile1','PageOutput','Y','N','9'),(15,'2','Page1_PrintDataSet02','ParamProfile1','PageOutput','Y','N','10'),(16,'2','Page1_PrintDataSet03','ParamProfile1','PageOutput','Y','N','11'),(17,'2','Page1_PrintDataSet04','ParamProfile1','PageOutput','Y','N','12'),(18,'2','Page1_PrintDataSet05','ParamProfile1','PageOutput','Y','N','13'),(19,'2','Page1_PrintDataSet06','ParamProfile1','PageOutput','Y','N','14'),(20,'2','Page1_PrintDataSet07','ParamProfile1','PageOutput','Y','N','15'),(21,'2','Page1_PrintConstDutDiff','ParamProfile1','PageOutput','Y','N','16'),(22,'2','Page2_PrintConstErrorPercent','ParamProfile1','PageOutput','Y','N','17'),(23,'2','Page2_PrintConstPulseCntResult','ParamProfile1','PageOutput','Y','N','18'),(24,'2','BookPrintHdr','ParamProfile1','BookPrint','N','Y','19'),(25,'2','BookPrintDataSet01','ParamProfile1','BookPrint','Y','N','20'),(26,'2','BookPrintDataSet02','ParamProfile1','BookPrint','Y','N','21'),(27,'2','BookPrintDataSet03','ParamProfile1','BookPrint','Y','N','22'),(28,'2','BookPrintDataSet04','ParamProfile1','BookPrint','Y','N','23'),(29,'2','BookPrintDataSet05','ParamProfile1','BookPrint','Y','N','24'),(30,'2','BookPrintDataSet06','ParamProfile1','BookPrint','Y','N','25'),(31,'2','BookPrintDataSet07','ParamProfile1','BookPrint','Y','N','26'),(32,'2','BookPrintConstErrorPercent','ParamProfile1','BookPrint','Y','N','27'),(33,'2','BookPrintDataSet09','ParamProfile1','BookOutput','Y','N','28'),(34,'2','BookPrintDataSet10','ParamProfile1','BookOutput','Y','N','29'),(35,'2','Page1_InputRepeatAvg','ParamProfile1','PageInput','Y','N','30'),(36,'2','BookPrintRepeatAverage','ParamProfile1','BookOutput','Y','N','31'),(37,'2','Page2_InputConstantTestError','ParamProfile1','PageInput','Y','N','32'),(38,'2','BookPrintConstantTestError','ParamProfile1','BookOutput','Y','N','33'),(39,'2','BookPrintConstantPulseError','ParamProfile1','BookOutput','Y','N','34');
/*!40000 ALTER TABLE `rp_operation_param_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rp_print_position`
--

DROP TABLE IF EXISTS `rp_print_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_print_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cell_position` varchar(45) DEFAULT NULL,
  `data_owner` varchar(45) DEFAULT NULL,
  `key_param` varchar(100) DEFAULT NULL,
  `populate_all_data` varchar(3) NOT NULL,
  `populate_only_headers` varchar(3) NOT NULL,
  `test_data_filter2` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA4269C4C76EA4A` (`test_data_filter2`),
  CONSTRAINT `FKA4269C4C76EA4A` FOREIGN KEY (`test_data_filter2`) REFERENCES `rp_test_data_filter` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=653 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_print_position`
--

LOCK TABLES `rp_print_position` WRITE;
/*!40000 ALTER TABLE `rp_print_position` DISABLE KEYS */;
INSERT INTO `rp_print_position` VALUES (305,'D8','TestDataFilter','MUT Error Result Value','Y','N',89),(306,'D5','TestDataFilter','Header1','N','Y',89),(309,'E8','TestDataFilter','MUT Error Result Value','Y','N',91),(310,'E5','TestDataFilter','Header1','N','Y',91),(311,'F8','TestDataFilter','MUT Error Result Value','Y','N',92),(312,'F5','TestDataFilter','Header1','N','Y',92),(313,'G8','TestDataFilter','MUT Error Result Value','Y','N',93),(314,'G7','TestDataFilter','Header1','N','Y',93),(321,'H8','TestDataFilter','MUT Error Result Value','Y','N',97),(322,'H7','TestDataFilter','Header1','N','Y',97),(323,'I8','TestDataFilter','MUT Error Result Value','Y','N',98),(324,'I7','TestDataFilter','Header1','N','Y',98),(325,'J8','TestDataFilter','MUT Error Result Value','Y','N',99),(326,'J7','TestDataFilter','Header1','N','Y',99),(333,'K8','TestDataFilter','MUT Error Result Value','Y','N',103),(334,'K7','TestDataFilter','Header1','N','Y',103),(335,'L8','TestDataFilter','MUT Error Result Value','Y','N',104),(336,'L7','TestDataFilter','Header1','N','Y',104),(337,'M8','TestDataFilter','MUT Error Result Value','Y','N',105),(338,'M7','TestDataFilter','Header1','N','Y',105),(339,'S8','TestDataFilter','MUT Error Result Value','Y','N',106),(340,'S7','TestDataFilter','Header1','N','Y',106),(343,'T8','TestDataFilter','MUT Error Result Value','Y','N',108),(344,'T7','TestDataFilter','Header1','N','Y',108),(345,'U8','TestDataFilter','MUT Error Result Value','Y','N',109),(346,'U7','TestDataFilter','Header1','N','Y',109),(347,'V8','TestDataFilter','MUT Error Result Value','Y','N',110),(348,'V7','TestDataFilter','Header1','N','Y',110),(349,'N8','TestDataFilter','Repeat Result Value-1','Y','N',111),(350,'O8','TestDataFilter','Repeat Result Value-2','Y','N',111),(351,'W8','TestDataFilter','MUT Repeat Average Value','Y','N',111),(352,'N7','TestDataFilter','Repeat Header-1','N','Y',111),(353,'O7','TestDataFilter','Repeat Header-2','N','Y',111),(354,'W7','TestDataFilter','Header1','N','Y',111),(355,'X8','TestDataFilter','MUT Repeat Average Status','Y','N',111),(356,'X7','TestDataFilter','Header2','N','Y',111),(357,'S48','TestDataFilter','BookPrintRepeatAverage','Y','N',112),(358,'T48','TestDataFilter','BookOutputStatus','Y','N',112),(359,'S47','TestDataFilter','Header1','N','Y',112),(360,'T47','TestDataFilter','Header2','N','Y',112),(365,'I48','TestDataFilter','MUT Result Status','Y','N',114),(366,'H48','TestDataFilter','MUT Pulse Count','Y','N',114),(367,'H47','TestDataFilter','Header1','N','Y',114),(368,'I47','TestDataFilter','Header2','N','Y',114),(369,'K48','TestDataFilter','MUT Result Status','Y','N',115),(370,'J48','TestDataFilter','MUT Pulse Count','Y','N',115),(371,'J47','TestDataFilter','Header1','N','Y',115),(372,'K47','TestDataFilter','Header2','N','Y',115),(392,'P48','TestDataFilter','MUT Error Result Value','Y','N',118),(393,'U48','TestDataFilter','MUT Result Status','Y','N',118),(394,'L48','TestDataFilter','MUT Initial Register','Y','N',118),(395,'M48','TestDataFilter','MUT Final Register','Y','N',118),(396,'N48','TestDataFilter','MUT Difference','Y','N',118),(397,'P47','TestDataFilter','Header4','N','Y',118),(398,'U47','TestDataFilter','Header5','N','Y',118),(399,'J43','TestDataFilter','Rsm Initial','N','Y',118),(400,'N43','TestDataFilter','Rsm Final','N','Y',118),(401,'R43','TestDataFilter','Rsm Difference','N','Y',118),(402,'Y8','TestDataFilter','BookPrintConstantTestError','Y','N',119),(403,'Z8','TestDataFilter','BookOutputStatus','Y','N',119),(404,'Y7','TestDataFilter','Header4','N','Y',119),(405,'Z7','TestDataFilter','Header5','N','Y',119),(422,'O48','TestDataFilter','MUT Pulse Count','Y','N',126),(423,'O47','TestDataFilter','Header1','N','Y',126),(432,'AA8','TestDataFilter','BookPrintConstantPulseError','Y','N',131),(433,'AA7','TestDataFilter','Header1','N','Y',131),(436,'W48','TestDataFilter','PageOutputStatus','Y','N',130),(437,'W47','TestDataFilter','Header2','N','Y',130),(438,'AB8','TestDataFilter','BookOutputStatus','Y','N',131),(439,'AB7','TestDataFilter','Header2','N','Y',131),(440,'D34','TestDataFilter','Test Period','N','Y',89),(441,'D35','TestDataFilter','Target Voltage','N','Y',89),(442,'D36','TestDataFilter','Target Current','N','Y',89),(443,'D37','TestDataFilter','Target PF','N','Y',89),(444,'E34','TestDataFilter','Test Period','N','Y',91),(445,'E35','TestDataFilter','Warmup Period','N','Y',91),(446,'E36','TestDataFilter','Target Voltage','N','Y',91),(447,'E37','TestDataFilter','Target Current','N','Y',91),(448,'E38','TestDataFilter','Target PF','N','Y',91),(449,'F34','TestDataFilter','Test Period','N','Y',92),(450,'F35','TestDataFilter','Warmup Period','N','Y',92),(451,'F36','TestDataFilter','Target Voltage','N','Y',92),(452,'F37','TestDataFilter','Target Current','N','Y',92),(453,'F38','TestDataFilter','Target PF','N','Y',92),(454,'G34','TestDataFilter','Test Period','N','Y',93),(455,'G35','TestDataFilter','Warmup Period','N','Y',93),(456,'G36','TestDataFilter','Target Voltage','N','Y',93),(457,'G37','TestDataFilter','Target Current','N','Y',93),(458,'G38','TestDataFilter','Target PF','N','Y',93),(459,'G34','TestDataFilter','Test Period','N','Y',93),(460,'G35','TestDataFilter','Warmup Period','N','Y',93),(461,'G36','TestDataFilter','Target Voltage','N','Y',93),(462,'G37','TestDataFilter','Target Current','N','Y',93),(463,'G38','TestDataFilter','Target PF','N','Y',93),(464,'H34','TestDataFilter','Test Period','N','Y',97),(465,'H35','TestDataFilter','Warmup Period','N','Y',97),(466,'H36','TestDataFilter','Target Voltage','N','Y',97),(467,'H37','TestDataFilter','Target Current','N','Y',97),(468,'H38','TestDataFilter','Target PF','N','Y',97),(469,'G34','TestDataFilter','Test Period','N','Y',93),(470,'G35','TestDataFilter','Warmup Period','N','Y',93),(471,'G36','TestDataFilter','Target Voltage','N','Y',93),(472,'G37','TestDataFilter','Target Current','N','Y',93),(473,'G38','TestDataFilter','Target PF','N','Y',93),(474,'H34','TestDataFilter','Test Period','N','Y',97),(475,'H35','TestDataFilter','Warmup Period','N','Y',97),(476,'H36','TestDataFilter','Target Voltage','N','Y',97),(477,'H37','TestDataFilter','Target Current','N','Y',97),(478,'H38','TestDataFilter','Target PF','N','Y',97),(479,'I34','TestDataFilter','Test Period','N','Y',98),(480,'I35','TestDataFilter','Warmup Period','N','Y',98),(481,'I36','TestDataFilter','Target Voltage','N','Y',98),(482,'I37','TestDataFilter','Target Current','N','Y',98),(483,'I38','TestDataFilter','Target PF','N','Y',98),(484,'G34','TestDataFilter','Test Period','N','Y',93),(485,'G35','TestDataFilter','Warmup Period','N','Y',93),(486,'G36','TestDataFilter','Target Voltage','N','Y',93),(487,'G37','TestDataFilter','Target Current','N','Y',93),(488,'G38','TestDataFilter','Target PF','N','Y',93),(489,'H34','TestDataFilter','Test Period','N','Y',97),(490,'H35','TestDataFilter','Warmup Period','N','Y',97),(491,'H36','TestDataFilter','Target Voltage','N','Y',97),(492,'H37','TestDataFilter','Target Current','N','Y',97),(493,'H38','TestDataFilter','Target PF','N','Y',97),(494,'I34','TestDataFilter','Test Period','N','Y',98),(495,'I35','TestDataFilter','Warmup Period','N','Y',98),(496,'I36','TestDataFilter','Target Voltage','N','Y',98),(497,'I37','TestDataFilter','Target Current','N','Y',98),(498,'I38','TestDataFilter','Target PF','N','Y',98),(499,'J34','TestDataFilter','Test Period','N','Y',99),(500,'J35','TestDataFilter','Warmup Period','N','Y',99),(501,'J36','TestDataFilter','Target Voltage','N','Y',99),(502,'J37','TestDataFilter','Target Current','N','Y',99),(503,'J38','TestDataFilter','Target PF','N','Y',99),(504,'K34','TestDataFilter','Test Period','N','Y',103),(505,'K35','TestDataFilter','Warmup Period','N','Y',103),(506,'K36','TestDataFilter','Target Voltage','N','Y',103),(507,'K37','TestDataFilter','Target Current','N','Y',103),(508,'K38','TestDataFilter','Target PF','N','Y',103),(509,'K34','TestDataFilter','Test Period','N','Y',103),(510,'K35','TestDataFilter','Warmup Period','N','Y',103),(511,'K36','TestDataFilter','Target Voltage','N','Y',103),(512,'K37','TestDataFilter','Target Current','N','Y',103),(513,'K38','TestDataFilter','Target PF','N','Y',103),(514,'S34','TestDataFilter','Test Period','N','Y',106),(515,'S35','TestDataFilter','Warmup Period','N','Y',106),(516,'S36','TestDataFilter','Target Voltage','N','Y',106),(517,'S37','TestDataFilter','Target Current','N','Y',106),(518,'S38','TestDataFilter','Target PF','N','Y',106),(519,'S39','TestDataFilter','Target Frequency','N','Y',106),(520,'K34','TestDataFilter','Test Period','N','Y',103),(521,'K35','TestDataFilter','Warmup Period','N','Y',103),(522,'K36','TestDataFilter','Target Voltage','N','Y',103),(523,'K37','TestDataFilter','Target Current','N','Y',103),(524,'K38','TestDataFilter','Target PF','N','Y',103),(525,'S34','TestDataFilter','Test Period','N','Y',106),(526,'S35','TestDataFilter','Warmup Period','N','Y',106),(527,'S36','TestDataFilter','Target Voltage','N','Y',106),(528,'S37','TestDataFilter','Target Current','N','Y',106),(529,'S38','TestDataFilter','Target PF','N','Y',106),(530,'S39','TestDataFilter','Target Frequency','N','Y',106),(531,'T34','TestDataFilter','Test Period','N','Y',108),(532,'T35','TestDataFilter','Warmup Period','N','Y',108),(533,'T36','TestDataFilter','Target Voltage','N','Y',108),(534,'T37','TestDataFilter','Target Current','N','Y',108),(535,'T38','TestDataFilter','Target PF','N','Y',108),(536,'K34','TestDataFilter','Test Period','N','Y',103),(537,'K35','TestDataFilter','Warmup Period','N','Y',103),(538,'K36','TestDataFilter','Target Voltage','N','Y',103),(539,'K37','TestDataFilter','Target Current','N','Y',103),(540,'K38','TestDataFilter','Target PF','N','Y',103),(541,'S34','TestDataFilter','Test Period','N','Y',106),(542,'S35','TestDataFilter','Warmup Period','N','Y',106),(543,'S36','TestDataFilter','Target Voltage','N','Y',106),(544,'S37','TestDataFilter','Target Current','N','Y',106),(545,'S38','TestDataFilter','Target PF','N','Y',106),(546,'S39','TestDataFilter','Target Frequency','N','Y',106),(547,'T34','TestDataFilter','Test Period','N','Y',108),(548,'T35','TestDataFilter','Warmup Period','N','Y',108),(549,'T36','TestDataFilter','Target Voltage','N','Y',108),(550,'T37','TestDataFilter','Target Current','N','Y',108),(551,'T38','TestDataFilter','Target PF','N','Y',108),(552,'U34','TestDataFilter','Test Period','N','Y',109),(553,'U35','TestDataFilter','Warmup Period','N','Y',109),(554,'U36','TestDataFilter','Target Voltage','N','Y',109),(555,'U37','TestDataFilter','Target Current','N','Y',109),(556,'U38','TestDataFilter','Target PF','N','Y',109),(557,'K34','TestDataFilter','Test Period','N','Y',103),(558,'K35','TestDataFilter','Warmup Period','N','Y',103),(559,'K36','TestDataFilter','Target Voltage','N','Y',103),(560,'K37','TestDataFilter','Target Current','N','Y',103),(561,'K38','TestDataFilter','Target PF','N','Y',103),(562,'S34','TestDataFilter','Test Period','N','Y',106),(563,'S35','TestDataFilter','Warmup Period','N','Y',106),(564,'S36','TestDataFilter','Target Voltage','N','Y',106),(565,'S37','TestDataFilter','Target Current','N','Y',106),(566,'S38','TestDataFilter','Target PF','N','Y',106),(567,'S39','TestDataFilter','Target Frequency','N','Y',106),(568,'T34','TestDataFilter','Test Period','N','Y',108),(569,'T35','TestDataFilter','Warmup Period','N','Y',108),(570,'T36','TestDataFilter','Target Voltage','N','Y',108),(571,'T37','TestDataFilter','Target Current','N','Y',108),(572,'T38','TestDataFilter','Target PF','N','Y',108),(573,'U34','TestDataFilter','Test Period','N','Y',109),(574,'U35','TestDataFilter','Warmup Period','N','Y',109),(575,'U36','TestDataFilter','Target Voltage','N','Y',109),(576,'U37','TestDataFilter','Target Current','N','Y',109),(577,'U38','TestDataFilter','Target PF','N','Y',109),(584,'K34','TestDataFilter','Test Period','N','Y',103),(585,'K35','TestDataFilter','Warmup Period','N','Y',103),(586,'K36','TestDataFilter','Target Voltage','N','Y',103),(587,'K37','TestDataFilter','Target Current','N','Y',103),(588,'K38','TestDataFilter','Target PF','N','Y',103),(589,'S34','TestDataFilter','Test Period','N','Y',106),(590,'S35','TestDataFilter','Warmup Period','N','Y',106),(591,'S36','TestDataFilter','Target Voltage','N','Y',106),(592,'S37','TestDataFilter','Target Current','N','Y',106),(593,'S38','TestDataFilter','Target PF','N','Y',106),(594,'S39','TestDataFilter','Target Frequency','N','Y',106),(595,'T34','TestDataFilter','Test Period','N','Y',108),(596,'T35','TestDataFilter','Warmup Period','N','Y',108),(597,'T36','TestDataFilter','Target Voltage','N','Y',108),(598,'T37','TestDataFilter','Target Current','N','Y',108),(599,'T38','TestDataFilter','Target PF','N','Y',108),(600,'U34','TestDataFilter','Test Period','N','Y',109),(601,'U35','TestDataFilter','Warmup Period','N','Y',109),(602,'U36','TestDataFilter','Target Voltage','N','Y',109),(603,'U37','TestDataFilter','Target Current','N','Y',109),(604,'U38','TestDataFilter','Target PF','N','Y',109),(605,'V34','TestDataFilter','Test Period','N','Y',110),(606,'V35','TestDataFilter','Warmup Period','N','Y',110),(607,'V36','TestDataFilter','Target Voltage','N','Y',110),(608,'V37','TestDataFilter','Target Current','N','Y',110),(609,'V38','TestDataFilter','Target PF','N','Y',110),(610,'V39','TestDataFilter','Target Frequency','N','Y',110),(611,'N34','TestDataFilter','Test Period','N','Y',111),(612,'N35','TestDataFilter','Warmup Period','N','Y',111),(613,'N36','TestDataFilter','Target Voltage','N','Y',111),(614,'N37','TestDataFilter','Target Current','N','Y',111),(615,'N38','TestDataFilter','Target PF','N','Y',111),(616,'H74','TestDataFilter','Test Period','N','Y',114),(617,'H75','TestDataFilter','Warmup Period','N','Y',114),(618,'H76','TestDataFilter','Target Voltage','N','Y',114),(619,'H77','TestDataFilter','Target Current','N','Y',114),(620,'H78','TestDataFilter','Target PF','N','Y',114),(621,'H74','TestDataFilter','Test Period','N','Y',114),(622,'H75','TestDataFilter','Warmup Period','N','Y',114),(623,'H76','TestDataFilter','Target Voltage','N','Y',114),(624,'H77','TestDataFilter','Target Current','N','Y',114),(625,'H78','TestDataFilter','Target PF','N','Y',114),(626,'J74','TestDataFilter','Test Period','N','Y',115),(627,'J75','TestDataFilter','Warmup Period','N','Y',115),(628,'J76','TestDataFilter','Target Voltage','N','Y',115),(629,'J77','TestDataFilter','Target Current','N','Y',115),(630,'J78','TestDataFilter','Target PF','N','Y',115),(631,'H74','TestDataFilter','Test Period','N','Y',114),(632,'H75','TestDataFilter','Warmup Period','N','Y',114),(633,'H76','TestDataFilter','Target Voltage','N','Y',114),(634,'H77','TestDataFilter','Target Current','N','Y',114),(635,'H78','TestDataFilter','Target PF','N','Y',114),(636,'J74','TestDataFilter','Test Period','N','Y',115),(637,'J75','TestDataFilter','Warmup Period','N','Y',115),(638,'J76','TestDataFilter','Target Voltage','N','Y',115),(639,'J77','TestDataFilter','Target Current','N','Y',115),(640,'J78','TestDataFilter','Target PF','N','Y',115),(641,'L74','TestDataFilter','Test Period','N','Y',118),(642,'L75','TestDataFilter','Warmup Period','N','Y',118),(643,'L76','TestDataFilter','Target Voltage','N','Y',118),(644,'L77','TestDataFilter','Target Current','N','Y',118),(645,'L78','TestDataFilter','Target PF','N','Y',118),(646,'L79','TestDataFilter','Target Energy','N','Y',118);
/*!40000 ALTER TABLE `rp_print_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rp_test_data_filter`
--

DROP TABLE IF EXISTS `rp_test_data_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_test_data_filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `current_filter_unit_value` varchar(45) DEFAULT NULL,
  `current_percent_filter_data` varchar(45) DEFAULT NULL,
  `current_percent_filter_user_entry` varchar(45) DEFAULT NULL,
  `custom_test_name_data` varchar(45) DEFAULT NULL,
  `custom_test_name_user_entry` varchar(45) DEFAULT NULL,
  `energy_filter_data` varchar(45) DEFAULT NULL,
  `energy_filter_unit_value` varchar(45) DEFAULT NULL,
  `energy_filter_user_entry` varchar(45) DEFAULT NULL,
  `filter_active` varchar(3) NOT NULL,
  `filter_preview` varchar(45) DEFAULT NULL,
  `freq_filter_data` varchar(45) DEFAULT NULL,
  `freq_filter_unit_value` varchar(45) DEFAULT NULL,
  `freq_filter_user_entry` varchar(45) DEFAULT NULL,
  `header1_value` varchar(45) DEFAULT NULL,
  `header2_value` varchar(45) DEFAULT NULL,
  `header3_current_filter_selected` varchar(3) NOT NULL,
  `header3_custom_selected` varchar(3) NOT NULL,
  `header3_energy_filter_selected` varchar(3) NOT NULL,
  `header3_freq_filter_selected` varchar(3) NOT NULL,
  `populate_iteration_header_selected` varchar(3) NOT NULL,
  `header3_iteration_reading_id_selected` varchar(3) NOT NULL,
  `header3_pf_filter_selected` varchar(3) NOT NULL,
  `header3_value` varchar(45) DEFAULT NULL,
  `header3_volt_filter_selected` varchar(3) NOT NULL,
  `iteration_reading_start_id_user_entry` varchar(45) DEFAULT NULL,
  `iteration_reading_prefix_value` varchar(45) DEFAULT NULL,
  `non_displayed_data_set` varchar(45) DEFAULT NULL,
  `operation_compare_limits_selected` varchar(3) NOT NULL,
  `operation_input_key` varchar(45) DEFAULT NULL,
  `operation_input_only_header` varchar(3) NOT NULL,
  `operation_input_selected` varchar(3) NOT NULL,
  `operation_merge_limits_selected` varchar(3) NOT NULL,
  `operation_mode` varchar(45) DEFAULT NULL,
  `operation_none_selected` varchar(3) NOT NULL,
  `operation_output_selected` varchar(3) NOT NULL,
  `operation_process_local_compared_status` varchar(45) DEFAULT NULL,
  `operation_process_local_lower_limit` varchar(45) DEFAULT NULL,
  `operation_process_local_output_key` varchar(45) DEFAULT NULL,
  `operation_process_local_output_only_header` varchar(3) NOT NULL,
  `operation_process_local_result` varchar(45) DEFAULT NULL,
  `operation_process_local_upper_limit` varchar(45) DEFAULT NULL,
  `operation_process_master_compared_status` varchar(45) DEFAULT NULL,
  `operation_process_method` varchar(45) DEFAULT NULL,
  `operation_process_post_active` varchar(3) NOT NULL,
  `operation_process_post_input_value` varchar(45) DEFAULT NULL,
  `operation_process_post_method` varchar(45) DEFAULT NULL,
  `page_number` varchar(45) DEFAULT NULL,
  `pf_filter_data` varchar(45) DEFAULT NULL,
  `pf_filter_unit_value` varchar(45) DEFAULT NULL,
  `pf_filter_user_entry` varchar(45) DEFAULT NULL,
  `populate_operation_compared_local_status` varchar(3) NOT NULL,
  `populate_operation_compared_master_status` varchar(3) NOT NULL,
  `populate_operation_local_output` varchar(3) NOT NULL,
  `populate_operation_lower_limit` varchar(3) NOT NULL,
  `populate_operation_master_output` varchar(3) NOT NULL,
  `populate_operation_upper_limit` varchar(3) NOT NULL,
  `populate_result_status` varchar(3) NOT NULL,
  `replicate_count_value` varchar(45) DEFAULT NULL,
  `replicate_data` varchar(3) NOT NULL,
  `report_base_template` varchar(45) DEFAULT NULL,
  `result_data_type` varchar(45) DEFAULT NULL,
  `sub_test_type_selected` varchar(45) DEFAULT NULL,
  `table_serial_no` varchar(45) DEFAULT NULL,
  `test_execution_result_type_selected` varchar(45) DEFAULT NULL,
  `test_filter_data_populate_type` varchar(45) DEFAULT NULL,
  `test_filter_name` varchar(45) DEFAULT NULL,
  `test_type_alias` varchar(45) DEFAULT NULL,
  `test_type_selected` varchar(45) DEFAULT NULL,
  `user_comment_value` varchar(45) DEFAULT NULL,
  `volt_filter_unit_value` varchar(45) DEFAULT NULL,
  `volt_percent_filter_data` varchar(45) DEFAULT NULL,
  `volt_percent_filter_user_entry` varchar(45) DEFAULT NULL,
  `rp_manage` int(11) DEFAULT NULL,
  `iteration_reading_end_id_user_entry` varchar(45) DEFAULT NULL,
  `operation_source_result_type_selected` varchar(45) DEFAULT NULL,
  `header4_value` varchar(45) DEFAULT NULL,
  `header5_value` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2D48F561A4C2B18F` (`rp_manage`),
  CONSTRAINT `FK2D48F561A4C2B18F` FOREIGN KEY (`rp_manage`) REFERENCES `rp_manage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_test_data_filter`
--

LOCK TABLES `rp_test_data_filter` WRITE;
/*!40000 ALTER TABLE `rp_test_data_filter` DISABLE KEYS */;
INSERT INTO `rp_test_data_filter` VALUES (89,'Ib','1.0Ib','1.0','','','','kWh','','Y','VV_XX-80U-1.0-1.0Ib','','Hz','','VV 80%U','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','1','ResultDataKey','Vertical','VV','VV','Voltage Variation','','U','80U','80',1,'','MUT Error Result Value','',''),(91,'Ib','1.0Ib','1.0','','','','kWh','','Y','RPSNORM_XX-100U-1.0-1.0Ib','','Hz','','RPS-Nornal','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','Normal','2','ResultDataKey','Vertical','RPS_NORMAL','RPSNORM','Reverse Phase Sequence','','U','100U','100',1,'','MUT Error Result Value','',''),(92,'Ib','1.0Ib','1.0','','','','kWh','','Y','RPSREV_XX-100U-1.0-1.0Ib','','Hz','','RPS-Rev','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','Reverse','3','ResultDataKey','Vertical','RPS_REVERSE','RPSREV','Reverse Phase Sequence','','U','100U','100',1,'','MUT Error Result Value','',''),(93,'Imax','1.0Imax','1.0','','','','kWh','','N','LOE_XX-100U-1.0-1.0Imax','','Hz','','Imax UPF','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','4','ResultDataKey','Vertical','ImaxUPF','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(97,'Ib','1.0Ib','1.0','','','','kWh','','Y','LOE_XX-100U-1.0-1.0Ib','','Hz','','Ib UPF','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','5','ResultDataKey','Vertical','LOE_IB_UPF','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(98,'Ib','1.0Ib','1.0','','','','kWh','','Y','LOE_XX-100U-0.5L-1.0Ib','','Hz','','Ib 0.5L','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','0.5L','L','0.5','N','N','N','N','N','N','N','1','N','Base Template1','Error','','6','ResultDataKey','Vertical','LOE_IB_0_5_L','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(99,'Ib','1.0Ib','1.0','','','','kWh','','Y','LOE_XX-100U-0.8C-1.0Ib','','Hz','','Ib 0.8C','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','0.8C','C','0.8','N','N','N','N','N','N','N','1','N','Base Template1','Error','','7','ResultDataKey','Vertical','LOE_IB_0_8_C','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(103,'Ib','0.5Ib','0.5','','','','kWh','','Y','LOE_XX-100U-1.0-0.5Ib','','Hz','','50% Ib UPF','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','8','ResultDataKey','Vertical','LOE_50Ib_UPF','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(104,'Ib','0.5Ib','0.5','','','','kWh','','Y','LOE_XX-100U-0.5L-0.5Ib','','Hz','','50% Ib 0.5L','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','0.5L','L','0.5','N','N','N','N','N','N','N','1','N','Base Template1','Error','','9','ResultDataKey','Vertical','LOE_50Ib_0_5L','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(105,'Ib','0.5Ib','0.5','','','','kWh','','Y','LOE_XX-100U-0.8C-0.5Ib','','Hz','','50% Ib 0.8C','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','0.8C','C','0.8','N','N','N','N','N','N','N','1','N','Base Template1','Error','','10','ResultDataKey','Vertical','LOE_50Ib_0_8C','LOE','Accuracy','','U','100U','100',1,'','MUT Error Result Value','',''),(106,'Ib','1.0Ib','1.0','','','','kWh','','Y','FV_XX-100U-1.0-1.0Ib-52.5','52.5','Hz','52.5','Freq 52.5','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','11','ResultDataKey','Vertical','Freq_52_5','FV','Frequency Variation','','U','100U','100',1,'','MUT Error Result Value','',''),(108,'Ib','1.0Ib','1.0','','','','kWh','','Y','VU_XX-RY:100U-1.0-1.0Ib','','Hz','','VU RY','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','12','ResultDataKey','Vertical','VU_RY','VU','Voltage Unbalance','','RY:%U','RY:100U','100',1,'','MUT Error Result Value','',''),(109,'Ib','1.0Ib','1.0','','','','kWh','','Y','VU_XX-Y:100U-1.0-1.0Ib','','Hz','','VU Y','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','13','ResultDataKey','Vertical','VU_Y','VU','Voltage Unbalance','','Y:%U','Y:100U','100',1,'','MUT Error Result Value','',''),(110,'Ib','','','CSTMCustom_01','Custom_01','','kWh','','N','CSTM_Custom_01XX-1.0','','Hz','','Custom Result','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','LocalOutputStatus','','Page1_PrintConstDutDiff','N','','','MasterOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','14','ResultDataKey','Vertical','Custom_01','CSTM','Custom Test','','U','','',1,'','MUT Error Result Value','',''),(111,'Ib','1.0Ib','1.0','','','','kWh','','Y','REP_XX-100U-1.0-1.0Ib-1-2','','Hz','','Repeat Average Error','Repeat Average Status','N','N','N','N','Y','N','N','','N','1','Repeat-','Page1_InputRepeatAvg','N','Page1_InputRepeatAvg','N','Y','N','Input','N','N','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','1','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','15','ResultDataKey','Vertical','Repeat_Iteration','REP','Repeatability','','U','100U','100',1,'2','MUT Average','',''),(112,'Ib','','','','','','kWh','','Y','NA','','Hz','','Repeat Avg Error','Repeat Avg Status','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','Output','N','Y','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','1','','UPF','','N','Y','N','N','Y','N','N','1','N','Base Template1','MasterOutputSetData','','16','Operation','Vertical','Repeat_Iteration_Book',NULL,'None','','U','','',1,'','Operation','',''),(114,'Ib','0.004Ib','0.004','','','','kWh','','N','STA_XX-100U-1.0-0.004Ib','','Hz','','STA Pulse Count','STA Status','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','2','1.0','UPF','1.0','N','N','N','N','N','N','Y','1','N','Base Template1','Error','','17','ResultDataKey','Vertical','STA','STA','Starting Current','','U','100U','100',1,'','MUT Error Result Value','',''),(115,'Ib','','','','','','kWh','','Y','NLD_XX-115U-1.0','','Hz','','NLD Pulse Count','NLD Status','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','None','Y','N','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','2','1.0','UPF','1.0','N','N','N','N','N','N','Y','1','N','Base Template1','Error','','18','ResultDataKey','Vertical','NoLoad','NLD','No Load Test','','U','115U','115',1,'','MUT Error Result Value','',''),(118,'Ib','1.0Ib','1.0','','','0.1','kWh','0.1','Y','CONST_XX-100U-1.0-1.0Ib-0.1','','Hz','','','','N','N','N','N','N','N','N','','N','','','Page2_InputConstantTestError','N','Page2_InputConstantTestError','N','Y','N','Input','N','N','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','2','1.0','UPF','1.0','N','N','N','N','N','N','Y','1','N','Base Template1','Error','','19','ResultDataKey','Vertical','ConstantErrorPagePrint','CONST','Constant Test','','U','100U','100',1,'','MUT Error Result Value','Constant %Error','Constant Error Status'),(119,'Ib','','','','','','kWh','','Y','NA','','Hz','','','','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','Output','N','Y','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','1','','UPF','','N','Y','N','N','Y','N','N','1','N','Base Template1','MasterOutputSetData','','20','Operation','Vertical','ConstantErrorBookPrint',NULL,'None','','U','','',1,'','Operation','Const %Error','Const Error Status'),(126,'Ib','1.0Ib','1.0','','','0.1','kWh','0.1','Y','CONST_XX-100U-1.0-1.0Ib-0.1','','Hz','','Constant Pulse Count','','N','N','N','N','N','N','N','','N','','','Page2_InputConstPulseCnt','N','Page2_InputConstPulseCnt','N','Y','N','Input','N','N','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','2','1.0','UPF','1.0','N','N','N','N','N','N','N','1','N','Base Template1','Error','','21','ResultDataKey','Vertical','ConstPulsePagePrint','CONST','Constant Test','','U','100U','100',1,'','MUT Pulse Count','',''),(130,'Ib','','','','','','kWh','','Y','NA','','Hz','','','Constant Pulse Status','N','N','N','N','N','N','N','','N','','','','Y','Page1_InputDataSet05','N','N','N','Output','N','Y','PageOutputStatus','10.0','Page2_PrintConstPulseCntResult','N','','11.0','BookOutputStatus','Add','N','','Add','2','','UPF','','Y','N','N','N','N','N','N','1','N','Base Template1','LocalOutputSetStatus','','22','Operation','Vertical','ConstantPulseCountPageStatus',NULL,'None','','U','','',1,'','Operation','',''),(131,'Ib','','','','','','kWh','','Y','NA','','Hz','','Const Pulse Result','Const Pulse Status','N','N','N','N','N','N','N','','N','','','','N','Page1_InputDataSet05','N','N','N','Output','N','Y','PageOutputStatus','','Page1_PrintConstDutDiff','N','','','BookOutputStatus','None','N','','Add','1','','UPF','','N','Y','N','N','Y','N','N','1','N','Base Template1','MasterOutputSetData','','23','Operation','Vertical','ConstPulseCntBookStatus',NULL,'None','','U','','',1,'','Operation','','');
/*!40000 ALTER TABLE `rp_test_data_filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rp_test_filter_operation_process`
--

DROP TABLE IF EXISTS `rp_test_filter_operation_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rp_test_filter_operation_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `added_to_input_process` varchar(3) NOT NULL,
  `local_compared_status` varchar(45) DEFAULT NULL,
  `lower_limit` varchar(45) DEFAULT NULL,
  `master_compared_status` varchar(45) DEFAULT NULL,
  `operation_process_data_type` varchar(45) DEFAULT NULL,
  `operation_process_key` varchar(100) DEFAULT NULL,
  `populate_only_headers` varchar(3) NOT NULL,
  `result_value` varchar(45) DEFAULT NULL,
  `upper_limit` varchar(45) DEFAULT NULL,
  `test_data_filter1` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5C8BFF5C4C76EA49` (`test_data_filter1`),
  CONSTRAINT `FK5C8BFF5C4C76EA49` FOREIGN KEY (`test_data_filter1`) REFERENCES `rp_test_data_filter` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rp_test_filter_operation_process`
--

LOCK TABLES `rp_test_filter_operation_process` WRITE;
/*!40000 ALTER TABLE `rp_test_filter_operation_process` DISABLE KEYS */;
INSERT INTO `rp_test_filter_operation_process` VALUES (97,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',89),(99,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',91),(100,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',92),(101,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',93),(105,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',97),(106,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',98),(107,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',99),(111,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',103),(112,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',104),(113,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',105),(114,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',106),(116,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',108),(117,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',109),(118,'N','LocalOutputStatus','','MasterOutputStatus','BookOutput','None','N','','',110),(119,'N','PageOutputStatus','','BookOutputStatus','BookOutput','None','N','','',111),(120,'N','PageOutputStatus','','BookOutputStatus','BookOutput','BookPrintRepeatAverage','N','','',112),(121,'Y',NULL,NULL,NULL,'PageInput','Page1_InputRepeatAvg','N',NULL,NULL,112),(123,'N','PageOutputStatus','','BookOutputStatus','BookOutput','None','N','','',114),(124,'N','PageOutputStatus','','BookOutputStatus','BookOutput','None','N','','',115),(127,'N','PageOutputStatus','','BookOutputStatus','BookOutput','None','N','','',118),(128,'N','PageOutputStatus','','BookOutputStatus','BookOutput','BookPrintConstantTestError','N','','',119),(129,'Y',NULL,NULL,NULL,'PageInput','Page2_InputConstantTestError','N',NULL,NULL,119),(138,'N','PageOutputStatus','','BookOutputStatus','BookOutput','None','N','','',126),(145,'N','PageOutputStatus','10.0','BookOutputStatus','BookOutput','None','N','','11.0',130),(146,'Y',NULL,NULL,NULL,'PageInput','Page2_InputConstPulseCnt','N',NULL,NULL,130),(147,'N','PageOutputStatus','','BookOutputStatus','BookOutput','BookPrintConstantPulseError','N','','',131),(148,'Y',NULL,NULL,NULL,'PageInput','Page2_PrintConstPulseCntResult','N',NULL,NULL,131);
/*!40000 ALTER TABLE `rp_test_filter_operation_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'procal_spring_v1_0_3'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-28 18:00:32
