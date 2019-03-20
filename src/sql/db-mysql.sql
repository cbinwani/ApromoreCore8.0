-- This is required to allow large predictive monitoring ML models to be uploaded
SET GLOBAL max_allowed_packet=1073741824;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `bpmn_item`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `principal_item_permission`;
DROP TABLE IF EXISTS `xes_item`;

CREATE TABLE `bpmn_item` (
  `id`                INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `xml_serialization` BLOB NOT NULL,
  
  PRIMARY KEY (`id`),
  CONSTRAINT `bpmn_item_fk_id` FOREIGN KEY (`id`) REFERENCES `item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `item` (
  `id`   INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(30) NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `principal_item_permission` (
  `id`                  INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `principal_name`      VARCHAR(250) NOT NULL,
  `principal_classname` VARCHAR(250) NOT NULL,
  `item_id`             INT(11) UNSIGNED NOT NULL,
  `permission`          VARCHAR(30) NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT `principal_item_permission_fk_item_id` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `xes_item` (
  `id`                INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `xml_serialization` LONGBLOB NOT NULL,
  
  PRIMARY KEY (`id`),
  CONSTRAINT `xes_item_fk_id` FOREIGN KEY (`id`) REFERENCES `item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;
