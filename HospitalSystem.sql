-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: HOSPITAL
-- ------------------------------------------------------
-- Server version	5.7.21

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
-- Table structure for table `T_BRXX`
--

DROP TABLE IF EXISTS `T_BRXX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_BRXX` (
  `BRBH` char(6) NOT NULL,
  `BRMC` char(10) NOT NULL,
  `DLKL` char(8) NOT NULL,
  `YCJE` decimal(10,2) NOT NULL,
  `DLRQ` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`BRBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_BRXX`
--

LOCK TABLES `T_BRXX` WRITE;
/*!40000 ALTER TABLE `T_BRXX` DISABLE KEYS */;
INSERT INTO `T_BRXX` VALUES ('000001','汤汇川','thcdmm',99896.00,'2018-05-05 06:25:05'),('000002','熊倩','xqdmm',4968.00,'2018-05-04 18:01:28'),('000003','杨词','ycdmm',5.00,'2018-06-04 23:39:25'),('000004','魏硕','wsdmm',1992.00,'2018-06-04 23:25:59'),('000005','何卓','hzdmm',2992.00,'2018-05-04 18:11:15'),('000006','胡东明','hdmdmm',3998.00,'2018-05-04 17:48:14');
/*!40000 ALTER TABLE `T_BRXX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `T_GHXX`
--

DROP TABLE IF EXISTS `T_GHXX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_GHXX` (
  `GHBH` char(6) NOT NULL,
  `HZBH` char(6) NOT NULL,
  `YSBH` char(6) NOT NULL,
  `BRBH` char(6) NOT NULL,
  `GHRC` int(11) NOT NULL,
  `THBZ` tinyint(1) NOT NULL,
  `GHFY` decimal(8,2) NOT NULL,
  `RQSJ` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`GHBH`),
  KEY `catid` (`HZBH`),
  KEY `docid` (`YSBH`),
  KEY `pid` (`BRBH`),
  CONSTRAINT `t_ghxx_ibfk_1` FOREIGN KEY (`HZBH`) REFERENCES `t_hzxx` (`HZBH`),
  CONSTRAINT `t_ghxx_ibfk_2` FOREIGN KEY (`YSBH`) REFERENCES `t_ksys` (`YSBH`),
  CONSTRAINT `t_ghxx_ibfk_3` FOREIGN KEY (`BRBH`) REFERENCES `t_brxx` (`BRBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_GHXX`
--

LOCK TABLES `T_GHXX` WRITE;
/*!40000 ALTER TABLE `T_GHXX` DISABLE KEYS */;
INSERT INTO `T_GHXX` VALUES ('000001','000004','000006','000004',0,0,2.00,'2018-06-04 23:25:16'),('000002','000006','000008','000004',1,0,2.00,'2018-06-04 23:25:59'),('000003','000001','000002','000003',2,0,1.00,'2018-06-04 23:38:17'),('000004','000001','000002','000003',3,0,1.00,'2018-06-04 23:38:19'),('000005','000004','000006','000003',4,0,2.00,'2018-06-04 23:38:39'),('000006','000002','000003','000003',5,0,100.00,'2018-06-04 23:39:06'),('000007','000002','000003','000003',6,0,100.00,'2018-06-04 23:39:25');
/*!40000 ALTER TABLE `T_GHXX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `T_HZXX`
--

DROP TABLE IF EXISTS `T_HZXX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_HZXX` (
  `HZBH` char(6) NOT NULL,
  `HZMC` char(12) NOT NULL,
  `PYZS` char(4) NOT NULL,
  `KSBH` char(6) NOT NULL,
  `SFZJ` tinyint(1) NOT NULL,
  `GHRS` int(11) NOT NULL,
  `GHFY` decimal(10,2) NOT NULL,
  PRIMARY KEY (`HZBH`),
  KEY `register_category_ibfk_1` (`KSBH`),
  CONSTRAINT `t_hzxx_ibfk_1` FOREIGN KEY (`KSBH`) REFERENCES `t_ksxx` (`KSBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_HZXX`
--

LOCK TABLES `T_HZXX` WRITE;
/*!40000 ALTER TABLE `T_HZXX` DISABLE KEYS */;
INSERT INTO `T_HZXX` VALUES ('000001','内科_普通','NKPT','000001',0,5,1.00),('000002','内科_专家','NKZJ','000001',1,2,100.00),('000003','外科_普通','WKPT','000002',0,5,1.00),('000004','外科_专家','WKZJ','000002',1,2,2.00),('000005','口腔科_普通','KQPT','000003',0,5,1.00),('000006','口腔科_专家','KQZJ','000003',1,2,2.00),('000007','眼科_普通','YKPT','000004',0,5,1.00),('000008','眼科_专家','YKZJ','000004',1,2,2.00),('000009','内分泌科_普通','FMPT','000005',0,5,1.00),('000010','内分泌科_专家','FMZJ','000005',1,2,2.00),('000011','急诊科_普通','JZPT','000006',0,5,1.00),('000012','急诊科_专家','JZZJ','000006',1,2,2.00),('000013','妇产科_普通','FCPT','000007',0,5,1.00),('000014','妇产科_专家','FCZJ','000007',1,2,2.00),('000015','肿瘤科_普通','ZLPT','000008',0,5,1.00),('000016','肿瘤科_专家','ZLZJ','000008',1,2,2.00);
/*!40000 ALTER TABLE `T_HZXX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `T_KSXX`
--

DROP TABLE IF EXISTS `T_KSXX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_KSXX` (
  `KSBH` char(6) NOT NULL,
  `KSMC` char(10) NOT NULL,
  `PYZS` char(8) NOT NULL,
  PRIMARY KEY (`KSBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_KSXX`
--

LOCK TABLES `T_KSXX` WRITE;
/*!40000 ALTER TABLE `T_KSXX` DISABLE KEYS */;
INSERT INTO `T_KSXX` VALUES ('000001','内科','NK'),('000002','外科','KW'),('000003','口腔科','KQK'),('000004','眼科','YK'),('000005','内分泌科','NFMK'),('000006','急诊科','JZK'),('000007','妇产科','FCK'),('000008','肿瘤科','ZLK');
/*!40000 ALTER TABLE `T_KSXX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `T_KSYS`
--

DROP TABLE IF EXISTS `T_KSYS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_KSYS` (
  `YSBH` char(6) NOT NULL,
  `KSBH` char(6) NOT NULL,
  `YSMC` char(10) NOT NULL,
  `PYZS` char(4) NOT NULL,
  `DLKL` char(8) NOT NULL,
  `SFZJ` tinyint(1) NOT NULL,
  `DLRQ` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`YSBH`),
  KEY `depid` (`KSBH`),
  CONSTRAINT `t_ksys_ibfk_1` FOREIGN KEY (`KSBH`) REFERENCES `t_ksxx` (`KSBH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_KSYS`
--

LOCK TABLES `T_KSYS` WRITE;
/*!40000 ALTER TABLE `T_KSYS` DISABLE KEYS */;
INSERT INTO `T_KSYS` VALUES ('000001','000001','张铭','ZM','zmdmm',0,'2018-04-24 14:41:10'),('000002','000001','吴桐','WT','wtdmm',0,'2018-04-24 14:41:10'),('000003','000001','程欢','CH','chdmm',1,'2018-06-03 11:42:43'),('000004','000002','邓一夫','DYF','dyfdmm',0,'2018-04-24 14:41:10'),('000005','000002','崔存泽','CCZ','zzcdmm',0,'2018-04-24 14:41:10'),('000006','000002','李睿光','LLG','llgdmm',1,'2018-04-24 14:41:10'),('000007','000003','李沐辰','LMC','lmcdmm',0,'2018-04-24 14:41:10'),('000008','000003','刘铭哲','LMZ','lmzdmm',1,'2018-04-24 14:41:10'),('000009','000004','刘昱廷','LYT','lytdmm',0,'2018-04-24 14:41:10'),('000010','000004','张树琦','ZSQ','zsqdmm',1,'2018-04-24 14:41:10'),('000011','000005','马玮良','MWL','mwldmm',0,'2018-04-24 14:41:10'),('000012','000005','张晓辉','ZXH','zxhdmm',1,'2018-04-24 14:41:10'),('000013','000006','董子恒','DZH','dzhdmm',0,'2018-04-24 14:41:10'),('000014','000006','黄志强','HZQ','hzqdmm',1,'2018-04-24 14:41:10'),('000015','000007','陈志浩','CZH','czhdmm',0,'2018-04-24 14:41:10'),('000016','000007','胡思勖','HSX','hsxdmm',1,'2018-04-24 14:41:10'),('000017','000008','彭达','PD','pddmm',0,'2018-04-24 14:41:10'),('000018','000008','张骏骅','ZJH','zjhdmm',1,'2018-04-24 14:41:10');
/*!40000 ALTER TABLE `T_KSYS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-06 11:30:42
