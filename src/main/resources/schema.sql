create schema sampledb;
 
/*Table structure for table data */
CREATE TABLE data (
  id INTEGER NOT NULL IDENTITY,
  year varchar(100) NOT NULL,
  make varchar(100) NOT NULL,
  model_en varchar(100) NOT NULL,
  model_fr varchar(100) default NULL)