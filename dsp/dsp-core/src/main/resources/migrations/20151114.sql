alter table advertisers add column cpc float default 0;
insert into advertisers(id, url, landing_page, name, budget, cpc) values(0, "adv1.com", "adv1.com/landing_page", "adv1", 1000000, 500);
insert into advertisers(id, url, landing_page, name, budget, cpc) values(1, "adv2.com", "adv2.com/landing_page", "adv2", 700000, 200);
insert into advertisers(id, url, landing_page, name, budget, cpc) values(2, "adv3.com", "adv3.com/landing_page", "adv3", 5000000, 500);
insert into advertisers(id, url, landing_page, name, budget, cpc) values(3, "adv4.com", "adv4.com/landing_page", "adv4", 1500000, 300);
insert into advertisers(id, url, landing_page, name, budget, cpc) values(4, "adv5.com", "adv5.com/landing_page", "adv5", 5000000, 500);

insert into advertiser_block(id, advertiser_id, url) values (0, 0, "MXxGCbvStDP.jp");
insert into advertiser_block(id, advertiser_id, url) values (1, 0, "MYgn.jp");
insert into advertiser_block(id, advertiser_id, url) values (2, 0, "pMybkvKkt.jp");
insert into advertiser_block(id, advertiser_id, url) values (3, 0, "SKNeBeQCp.jp");
insert into advertiser_block(id, advertiser_id, url) values (4, 1, "PtwsaAVosEeLBfowUjhIuYQyo.jp");
insert into advertiser_block(id, advertiser_id, url) values (5, 1, "NXBfsFMHV.jp");
insert into advertiser_block(id, advertiser_id, url) values (6, 1, "vWUjCeiFlPujYJKnwHuPTqx.jp");
insert into advertiser_block(id, advertiser_id, url) values (7, 1, "KYxOmPRbafMNrkWPtZM.jp");
insert into advertiser_block(id, advertiser_id, url) values (8, 2, "PtwsaAVosEeLBfowUjhIuYQyo.jp");
insert into advertiser_block(id, advertiser_id, url) values (9, 2, "NXBfsFMHV.jp");
insert into advertiser_block(id, advertiser_id, url) values (10, 2, "vWUjCeiFlPujYJKnwHuPTqx.jp");
insert into advertiser_block(id, advertiser_id, url) values (11, 2, "KYxOmPRbafMNrkWPtZM.jp");
insert into advertiser_block(id, advertiser_id, url) values (12, 3, "UFsVttaSyyJEOmw.jp");
insert into advertiser_block(id, advertiser_id, url) values (13, 3, "JBcaEOthLCWD.jp");
insert into advertiser_block(id, advertiser_id, url) values (14, 3, "TNqDybwk.jp");
insert into advertiser_block(id, advertiser_id, url) values (15, 3, "RPGmeLdgirOexJORGvRZJLm.jp");
insert into advertiser_block(id, advertiser_id, url) values (16, 4, "DftJxxSkaoUfdFRyY.jp");
insert into advertiser_block(id, advertiser_id, url) values (17, 4, "BQOGEHXGLYpFCjaaLfMpiW.jp");
insert into advertiser_block(id, advertiser_id, url) values (18, 4, "TxiluFDqQWbFKrKY.jp");
insert into advertiser_block(id, advertiser_id, url) values (19, 4, "KYxOmPRbafMNrkWPtZM.jp");


