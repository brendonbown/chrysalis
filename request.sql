insert into user_authorization
(credit_institution,
person_id,
informational_area,
effective_date,
date_time_granted,
granted_by_id,
date_time_revoked,
update_type,
clock_start_time,
clock_end_time,
allowable_domain,
limitation_type,
limitation_value
)
select
credit_institution,
--(
--select person_id from person where net_id = 'qpt22'),
--select person_id from person where net_id = 'mrg2'),
--select person_id from person where net_id = 'lmb263'),
--select person_id from person where net_id = 'garygsc'),
-- Ryan Ogden
'462273232',
--Student Information Applications
--'187251772',
--ES Admissions
--'168256912',
--Chantel Prows
--'055294582',
--Dan Brandt
--'509256362',
--Brent Ellingson needs ADVPROGRESSRPT
--'668202942',
informational_area,
--'GRADECHANGE',
sysdate,
sysdate,
granted_by_id,
sysdate,
'U',
clock_start_time,
clock_end_time,
allowable_domain,
limitation_type,
limitation_value
from user_authorization
where rownum = 1
--and informational_area = 'TRNCOLLEGE'
--and informational_area = 'CURRICULUM'
--and informational_area = 'REGELIGIBILITY'
--and informational_area = 'LDSADM'
--and informational_area = 'ADVPGMDEGREES'
--and informational_area = 'TRNMAIN'
--and informational_area = 'ACADCONTACTS'
--and informational_area = 'SECAUTH'
--and informational_area = 'ADVPGMCOMMITTEE'
--and informational_area = 'GRADADMLOG'
and informational_area = 'GRADADMOGS'
--and informational_area = 'PERSON'
--and informational_area = 'TESTSCORE'
--and informational_area = 'GRADECHANGE'
--and informational_area = 'CLASSSCHED'
--and informational_area = 'GRADAPPMNGR'
--and informational_area = 'ADVPROGRESSRPT'
--and informational_area = 'WAITLIST'
--and informational_area = 'REGPRIORITY'
--and informational_area = 'UNITREVIEW7'
--and informational_area = 'REGFLAGHOLDM'
--and informational_area = 'ENDORSE'
--and informational_area = 'CESHOLD'
--and informational_area = 'ADVPGMCLEAR'
--and informational_area = 'PERSON'
--and informational_area = 'SIGNATURES'
--and informational_area = 'OWNER-CURRICULUM'
--and informational_area = 'GRADFACULTY'
--and informational_area = 'ADVGSRPT'
--and informational_area = 'CLASSROLL'
--and informational_area = 'CLSSCHED'
--and informational_area = 'GRADEROLL'
--and informational_area = 'GRADEASSIST'
--and informational_area = 'GRADESUBMIT'
and sysdate between effective_date and nvl(expired_date,(sysdate + 1))
and update_type = 'U'
--and limitation_value is null
--and limitation_type = 'OGS'
-- this is Janice Robinson
and person_id = '578203002'
-- this is jearlene
--and person_id = (select person_id from person where net_id = 'jl8')
-- this is coral taylor
--and person_id = (select person_id from person where net_id = 'ct22')
;