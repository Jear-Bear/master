#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Contact: frony0@gmail.com
# Contact: @jodabeats (Discord)

# modified by solliss (sollniss@web.de)
# fixed bugs (as in, make it not crash on startup)
# made deck selection global
# added kanji kantei sorting
# made kanji kantei default

# Further modified by Raigeki, version "automatic"
# Just by clicking one button, it saves a kanji grid for each sorting method
# Filename and title of grid is current time, and a separate folder is created for each sorting method
# You can click the "generate" button every day and make a nice list of kanji grids that track your progress over time

# Ported to 2.1 by Joda
# Switched to use qconnect from aqt utils
# Fixed outdated functions
# Added some imports
# Moved some ui buttons from settings over to the generated grids
# Added naming function to generated grid windows for easier navigation
# Cleaned up settings menu for readability
# Optimized rendering algorithms for html and png generation
# PyQt is a pain to work with sometimes :(
# too lazy to type the rest but basically just made it work with 2.1

# Planning to integrate exporting html + png of kanji not yet in decks.

import time,codecs,math,os,unicodedata,datetime,operator
from aqt import mw
from aqt.utils import showInfo, qconnect
from anki.utils import ids2str
from anki.hooks import addHook
from aqt.webview import AnkiWebView
from aqt.qt import *
from functools import reduce

#KNOWN BUG IN ORIGINAL VERSION OF ADD ON: If it matches a field with no kanji (for example "audio"), it immediately discards the entire card and doesn't include any of its kanji in the grid - even if that card has other matched fields ("kanji", "Japanese") with kanji! 
#So try to use only one search term or only the defult ones included below and it should work fine. 
#If you insist on playing with this, try to only include "main fields" that either contain kanji or who should just be discarded anyway if they don't. Usually the main field on the front. Especially fields like "audio" and "extra notes" will break this add-on and they are usually not necessary!
#Also, avoid inputting the name of fields that contain example sentences. For example, "sentence" or "expression". This will GREATLY skew your results as it only looks at the entire card's overall maturity. 
#If in doubt, then you can probably just let this be.

# ------------------ vvv OVERWRITE CODE BELOW WITH CONTENT OF KANJI_GRID_SETTINGS.TXT vvv ------------------
_pattern = ["Kanji","Japanese","Front"]
_literal = True
_caseSensitive = False
_filePathBase = ""
_interval = 180
_kanjiPerRowPNG = 20
_chosenSortingMethods = ["jlpt", "jouyou", "kanken", "order found", "unicode", "score", "frequency"]
_printNotInDeck = True
_makePNG = True
_makeHTML = True
_saveKanjiNotInDeck = True
_onlySeenKanji = False
_suppressSettingsPopup = False
# ------------------ ^^^ OVERWRITE CODE ABOVE WITH CONTENT OF KANJI_GRID_SETTINGS.TXT ^^^ ------------------


#Changes from 0 through 7 during the runtime of the program - defines sorting method. Don't touch this
_group = 0 
_sortingMethods = ["jlpt", "jouyou", "kanken", "order found", "unicode", "score", "frequency"]
_tooltips = True
_kanjionly = True
_ignore = u"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" + \
          u"ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" + \
          u"ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ" + \
          u"1234567890１２３４５６７８９０" + \
          u"あいうゔえおぁぃぅぇぉかきくけこがぎぐげごさしすせそざじずぜぞ" + \
          u"たちつてとだぢづでどなにぬねのはひふへほばびぶべぼぱぴぷぺぽ" + \
          u"まみむめもやゃゆゅよょらりるれろわをんっ" + \
          u"アイウヴエオァィゥェォカキクケコガギグゲゴサシスセソザジズゼゾ" + \
          u"タチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポ" + \
          u"マミムメモヤャユュヨョラリルレロワヲンッ" + \
          u"!\"$%&'()|=~-^@[;:],./`{+*}<>?\\_" + \
          u"＠「；：」、。・‘｛＋＊｝＜＞？＼＿！”＃＄％＆’（）｜＝．〜～ー＾ ゙゙゚" + \
          u"☆★＊○●◎〇◯“…『』#♪ﾞ〉〈→》《π×"
_jlpt = [ (u'Not in our JLPT list', ''),
    (u'N5', u'日一国人年大十二本中長出三時行見月後前生五間上東四今金九入学高円子外八六下来気小七山話女北午百書先名川千水半男西電校語土木聞食車何南万毎白天母火右読友左休父雨'),
    (u'N4', u'会同事自社発者地業方新場員立開手力問代明動京目通言理体田主題意不作用度強公持野以思家世多正安院心界教文元重近考画海売知道集別物使品計死特私始朝運終台広住真有口少町料工建空急止送切転研足究楽起着店病質待試族銀早映親験英医仕去味写字答夜音注帰古歌買悪図週室歩風紙黒花春赤青館屋色走秋夏習駅洋旅服夕借曜飲肉貸堂鳥飯勉冬昼茶弟牛魚兄犬妹姉漢'),
    (u'N3', u'政議民連対部合市内相定回選米実関決全表戦経最現調化当約首法性要制治務成期取都和機平加受続進数記初指権支産点報済活原共得解交資予向際勝面告反判認参利組信在件側任引求所次昨論官増係感情投示変打直両式確果容必演歳争談能位置流格疑過局放常状球職与供役構割費付由説難優夫収断石違消神番規術備宅害配警育席訪乗残想声念助労例然限追商葉伝働形景落好退頭負渡失差末守若種美命福望非観察段横深申様財港識呼達良候程満敗値突光路科積他処太客否師登易速存飛殺号単座破除完降責捕危給苦迎園具辞因馬愛富彼未舞亡冷適婦寄込顔類余王返妻背熱宿薬険頼覚船途許抜便留罪努精散静婚喜浮絶幸押倒等老曲払庭徒勤遅居雑招困欠更刻賛抱犯恐息遠戻願絵越欲痛笑互束似列探逃遊迷夢君閉緒折草暮酒悲晴掛到寝暗盗吸陽御歯忘雪吹娘誤洗慣礼窓昔貧怒祖泳杯疲皆鳴腹煙眠怖耳頂箱晩寒髪忙才靴恥偶偉猫幾'),
    (u'N2', u'党協総区領県設改府査委軍団各島革村勢減再税営比防補境導副算輸述線農州武象域額欧担準賞辺造被技低復移個門課脳極含蔵量型況針専谷史階管兵接細効丸湾録省旧橋岸周材戸央券編捜竹超並療採森競介根販歴将幅般貿講林装諸劇河航鉄児禁印逆換久短油暴輪占植清倍均億圧芸署伸停爆陸玉波帯延羽固則乱普測豊厚齢囲卒略承順岩練軽了庁城患層版令角絡損募裏仏績築貨混昇池血温季星永著誌庫刊像香坂底布寺宇巨震希触依籍汚枚複郵仲栄札板骨傾届巻燃跡包駐弱紹雇替預焼簡章臓律贈照薄群秒奥詰双刺純翌快片敬悩泉皮漁荒貯硬埋柱祭袋筆訓浴童宝封胸砂塩賢腕兆床毛緑尊祝柔殿濃液衣肩零幼荷泊黄甘臣浅掃雲掘捨軟沈凍乳恋紅郊腰炭踊冊勇械菜珍卵湖喫干虫刷湯溶鉱涙匹孫鋭枝塗軒毒叫拝氷乾棒祈拾粉糸綿汗銅湿瓶咲召缶隻脂蒸肌耕鈍泥隅灯辛磨麦姓筒鼻粒詞胃畳膚机濯塔灰沸菓帽枯涼舟貝符憎皿肯燥畜挟曇滴伺'),
    (u'N1', u'氏統保第結派案策基価提挙応企検藤沢裁証援施井護展態鮮視条幹独宮率衛張監環審義訴株姿閣衆評影松撃佐核整融製票渉響推請器士討攻崎督授催及憲離激摘系批郎健盟従修隊織拡故振弁就異献厳維浜遺塁邦素遣抗模雄益緊標宣昭廃伊江僚吉盛皇臨踏壊債興源儀創障継筋闘葬避司康善逮迫惑崩紀聴脱級博締救執房撤削密措志載陣我為抑幕染奈傷択秀徴弾償功拠秘拒刑塚致繰尾描鈴盤項喪伴養懸街契掲躍棄邸縮還属慮枠恵露沖緩節需射購揮充貢鹿却端賃獲郡併徹貴衝焦奪災浦析譲称納樹挑誘紛至宗促慎控智握宙俊銭渋銃操携診託撮誕侵括謝駆透津壁稲仮裂敏是排裕堅訳芝綱典賀扱顧弘看訟戒祉誉歓奏勧騒閥甲縄郷揺免既薦隣華範隠徳哲杉釈己妥威豪熊滞微隆症暫忠倉彦肝喚沿妙唱阿索誠襲懇俳柄驚麻李浩剤瀬趣陥斎貫仙慰序旬兼聖旨即柳舎偽較覇詳抵脅茂犠旗距雅飾網竜詩繁翼潟敵魅嫌斉敷擁圏酸滅罰礎腐脚潮梅尽僕桜滑孤炎賠句鋼頑鎖彩摩励縦輝蓄軸巡稼瞬砲噴誇祥牲秩帝宏唆阻泰賄撲堀菊絞縁唯膨矢耐塾漏慶猛芳懲剣彰棋丁恒揚冒之倫陳憶潜梨仁克岳概拘墓黙須偏雰遇諮狭卓亀糧簿炉牧殊殖艦輩穴奇慢鶴謀暖昌拍朗寛覆胞泣隔浄没暇肺貞靖鑑飼陰銘随烈尋稿丹啓也丘棟壌漫玄粘悟舗妊熟旭恩騰往豆遂狂岐陛緯培衰艇屈径淡抽披廷錦准暑磯奨浸剰胆繊駒虚霊帳悔諭惨虐翻墜沼据肥徐糖搭盾脈滝軌俵妨擦鯨荘諾雷漂懐勘栽拐駄添冠斜鏡聡浪亜覧詐壇勲魔酬紫曙紋卸奮欄逸涯拓眼獄尚彫穏顕巧矛垣欺釣萩粛栗愚嘉遭架鬼庶稚滋幻煮姫誓把践呈疎仰剛疾征砕謡嫁謙后嘆菌鎌巣頻琴班棚潔酷宰廊寂辰霞伏碁俗漠邪晶墨鎮洞履劣那殴娠奉憂朴亭淳怪鳩酔惜穫佳潤悼乏該赴桑桂髄虎盆晋穂壮堤飢傍疫累痴搬晃癒桐寸郭尿凶吐宴鷹賓虜陶鐘憾猪紘磁弥昆粗訂芽庄傘敦騎寧循忍怠如寮祐鵬鉛珠凝苗獣哀跳匠垂蛇澄縫僧眺亘呉凡憩媛溝恭刈睡錯伯笹穀陵霧魂弊妃舶餓窮掌麗綾臭悦刃縛暦宜盲粋辱毅轄猿弦稔窒炊洪摂飽冗桃狩朱渦紳枢碑鍛刀鼓裸猶塊旋弓幣膜扇腸槽慈楊伐駿漬糾亮墳坪紺娯椿舌羅峡俸厘峰圭醸蓮弔乙汁尼遍衡薫猟羊款閲偵喝敢胎酵憤豚遮扉硫赦窃泡瑞又慨紡恨肪扶戯伍忌濁奔斗蘭迅肖鉢朽殻享秦茅藩沙輔媒鶏禅嘱胴迭挿嵐椎絹陪剖譜郁悠淑帆暁傑楠笛玲奴錠拳翔遷拙侍尺峠篤肇渇叔雌亨堪叙酢吟逓嶺甚喬崇漆岬癖愉寅礁乃洲屯樺槙姻巌擬塀唇睦閑胡幽峻曹詠卑侮鋳抹尉槻隷禍蝶酪茎帥逝汽琢匿襟蛍蕉寡琉痢庸朋坑藍賊搾畔遼唄孔橘漱呂拷嬢苑巽杜渓翁廉謹瞳湧欣窯褒醜升殉煩巴禎劾堕租稜桟倭婿慕斐罷矯某囚魁虹鴻泌於赳漸蚊葵厄藻禄孟嫡尭嚇巳凸暢韻霜硝勅芹杏棺儒鳳馨慧愁楼彬匡眉欽薪褐賜嵯綜繕栓翠鮎榛凹艶惣蔦錬隼渚衷逐斥稀芙詔皐雛惟佑耀黛渥憧宵妄惇脩甫酌蚕嬉蒼暉頒只肢檀凱彗謄梓丑嗣叶汐絢朔伽畝抄爽黎惰蛮冴萌旺壱偲瑠允侯蒔鯉弧遥舜瑛附彪卯但綺芋茜凌皓洸毬婆緋鯛怜邑倣碧啄穣酉悌倹柚繭亦詢采紗賦眸玖弐錘諄倖痘笙侃裟洵爾耗昴銑莞伶碩宥滉晏伎朕迪綸且竣晨吏燦麿頌箇楓琳梧哉澪匁晟衿凪梢丙颯茄勺恕蕗瑚遵瞭燎虞柊侑謁斤嵩捺蓉茉袈燿誼冶栞墾勁菖旦椋叡紬胤凜亥爵脹麟莉汰瑶瑳耶椰絃丞璃奎塑昂柾熙菫諒鞠崚濫捷'),
    ]
_jouyou = [ (u'Non-Jouyou and non-Jinmeiyou', ''),
    (u'Grade 1', u'一右雨円王音下火花貝学気休玉金九空月犬見五口校左三山四子糸字耳七車手十出女小上森人水正生青石赤先千川早草足村大男竹中虫町天田土二日入年白八百文本名木目夕立力林六'),
    (u'Grade 2', u'引羽雲園遠黄何夏家科歌画会回海絵外角楽活間丸岩顔帰汽記弓牛魚京強教近兄形計元原言古戸午後語交光公工広考行高合国黒今才細作算姉市思止紙寺時自室社弱首秋週春書少場色食心新親図数星晴声西切雪線船前組走多太体台谷知地池茶昼朝長鳥直通弟店点電冬刀東当答頭同道読内南肉馬買売麦半番父風分聞米歩母方北妹毎万明鳴毛門夜野矢友曜用来理里話'),
    (u'Grade 3', u'悪安暗委意医育員飲院運泳駅央横屋温化荷界開階寒感漢館岸期起客宮急球究級去橋業局曲銀区苦具君係軽決血研県庫湖向幸港号根祭坂皿仕使始指死詩歯事持次式実写者主取守酒受州拾終習集住重宿所暑助勝商昭消章乗植深申真神身進世整昔全想相送息速族他打対待代第題炭短談着柱注丁帳調追定庭笛鉄転登都度島投湯等豆動童農波配倍箱畑発反板悲皮美鼻筆氷表病秒品負部服福物平返勉放味命面問役薬油有由遊予様洋羊葉陽落流旅両緑礼列練路和'),
    (u'Grade 4', u'愛案以位囲胃衣印栄英塩億加果課貨芽改械害街各覚完官管観関願喜器希旗機季紀議救求泣給挙漁競共協鏡極訓軍郡型径景芸欠結健建験固候功好康航告差最菜材昨刷察札殺参散産残司史士氏試児治辞失借種周祝順初唱松焼照省笑象賞信臣成清静席積折節説戦浅選然倉巣争側束続卒孫帯隊達単置仲貯兆腸低停底的典伝徒努灯働堂得特毒熱念敗梅博飯費飛必標票不付夫府副粉兵別変辺便包法望牧末満未脈民無約勇要養浴利陸料良量輪類令例冷歴連労老録'),
    (u'Grade 5', u'圧易移因営永衛液益演往応恩仮価可河過賀解快格確額刊幹慣眼基寄規技義逆久旧居許境興均禁句群経潔件券検険減現限個故護効厚構耕講鉱混査再妻採災際在罪財桜雑賛酸師志支枝資飼似示識質舎謝授修術述準序承招証常情条状織職制勢性政精製税績責接設絶舌銭祖素総像増造則測属損態貸退団断築張提程敵適統導銅徳独任燃能破判版犯比肥非備俵評貧婦富布武復複仏編弁保墓報豊暴貿防務夢迷綿輸余預容率略留領'),
    (u'Grade 6', u'異遺域宇映延沿我灰拡閣革割株巻干看簡危揮机貴疑吸供胸郷勤筋敬系警劇激穴憲権絹厳源呼己誤后孝皇紅鋼降刻穀骨困砂座済裁策冊蚕姿私至視詞誌磁射捨尺若樹収宗就衆従縦縮熟純処署諸除傷将障城蒸針仁垂推寸盛聖誠宣専泉洗染善創奏層操窓装臓蔵存尊宅担探誕暖段値宙忠著庁潮頂賃痛展党糖討届難乳認納脳派俳拝背肺班晩否批秘腹奮並閉陛片補暮宝訪亡忘棒枚幕密盟模訳優郵幼欲翌乱卵覧裏律臨朗論'),
    (u'JuniorHS', u'亜哀握扱依偉威尉慰為維緯違井壱逸稲芋姻陰隠韻渦浦影詠鋭疫悦謁越閲宴援炎煙猿縁鉛汚凹奥押欧殴翁沖憶乙卸穏佳嫁寡暇架禍稼箇華菓蚊雅餓介塊壊怪悔懐戒拐皆劾慨概涯該垣嚇核殻獲穫較郭隔岳掛潟喝括渇滑褐轄且刈乾冠勘勧喚堪寛患憾換敢棺款歓汗環甘監緩缶肝艦貫還鑑閑陥含頑企奇岐幾忌既棋棄祈軌輝飢騎鬼偽儀宜戯擬欺犠菊吉喫詰却脚虐丘及朽窮糾巨拒拠虚距享凶叫峡恐恭挟況狂狭矯脅響驚仰凝暁斤琴緊菌襟謹吟駆愚虞偶遇隅屈掘靴繰桑勲薫傾刑啓契恵慶憩掲携渓継茎蛍鶏迎鯨撃傑倹兼剣圏堅嫌懸献肩謙賢軒遣顕幻弦玄孤弧枯誇雇顧鼓互呉娯御悟碁侯坑孔巧恒慌抗拘控攻更江洪溝甲硬稿絞綱肯荒衡貢購郊酵項香剛拷豪克酷獄腰込墾婚恨懇昆紺魂佐唆詐鎖債催宰彩栽歳砕斎載剤咲崎削搾索錯撮擦傘惨桟暫伺刺嗣施旨祉紫肢脂諮賜雌侍慈滋璽軸執湿漆疾芝赦斜煮遮蛇邪爵酌釈寂朱殊狩珠趣儒寿需囚愁秀臭舟襲酬醜充柔汁渋獣銃叔淑粛塾俊瞬准循旬殉潤盾巡遵庶緒叙徐償匠升召奨宵尚床彰抄掌昇晶沼渉焦症硝礁祥称粧紹肖衝訟詔詳鐘丈冗剰壌嬢浄畳譲醸錠嘱飾殖触辱伸侵唇娠寝審慎振浸紳薪診辛震刃尋甚尽迅陣酢吹帥炊睡粋衰遂酔随髄崇枢据杉澄瀬畝是姓征牲誓請逝斉隻惜斥析籍跡拙摂窃仙占扇栓潜旋繊薦践遷鮮漸禅繕塑措疎礎租粗訴阻僧双喪壮捜掃挿曹槽燥荘葬藻遭霜騒憎贈促即俗賊堕妥惰駄耐怠替泰滞胎袋逮滝卓択拓沢濯託濁諾但奪脱棚丹嘆淡端胆鍛壇弾恥痴稚致遅畜蓄逐秩窒嫡抽衷鋳駐弔彫徴懲挑眺聴超跳勅朕沈珍鎮陳津墜塚漬坪釣亭偵貞呈堤帝廷抵締艇訂逓邸泥摘滴哲徹撤迭添殿吐塗斗渡途奴怒倒凍唐塔悼搭桃棟盗痘筒到謄踏逃透陶騰闘洞胴峠匿督篤凸突屯豚曇鈍縄軟尼弐如尿妊忍寧猫粘悩濃把覇婆廃排杯輩培媒賠陪伯拍泊舶薄迫漠爆縛肌鉢髪伐罰抜閥伴帆搬畔繁般藩販範煩頒盤蛮卑妃彼扉披泌疲碑罷被避尾微匹姫漂描苗浜賓頻敏瓶怖扶敷普浮符腐膚譜賦赴附侮舞封伏幅覆払沸噴墳憤紛雰丙併塀幣弊柄壁癖偏遍舗捕穂募慕簿倣俸奉峰崩抱泡砲縫胞芳褒邦飽乏傍剖坊妨帽忙房某冒紡肪膨謀僕墨撲朴没堀奔翻凡盆摩磨魔麻埋膜又抹繭慢漫魅岬妙眠矛霧婿娘銘滅免茂妄猛盲網耗黙戻紋厄躍柳愉癒諭唯幽悠憂猶裕誘雄融与誉庸揚揺擁溶窯謡踊抑翼羅裸頼雷絡酪欄濫吏履痢離硫粒隆竜慮虜了僚寮涼猟療糧陵倫厘隣塁涙累励鈴隷零霊麗齢暦劣烈裂廉恋錬炉露廊楼浪漏郎賄惑枠湾腕'),
    (u'New Jouyou', u'挨宛闇椅畏萎茨咽淫臼唄餌怨艶旺岡臆俺苛牙崖蓋骸柿顎葛釜鎌瓦韓玩伎畿亀僅巾錦駒串窟熊稽詣隙桁拳鍵舷股虎乞勾喉梗頃痕沙挫塞采阪埼柵拶斬鹿叱嫉腫呪蹴拭尻芯腎須裾凄醒戚脊煎羨腺詮膳曽狙遡爽痩捉袖遜汰唾堆戴誰旦綻酎捗椎潰爪鶴諦溺填貼妬賭藤憧瞳栃頓奈那謎鍋匂虹捻罵剥箸斑氾汎眉膝肘媛阜蔽蔑蜂貌頬睦勃昧枕蜜冥麺餅冶弥湧妖沃嵐藍梨璃侶瞭瑠呂賂弄麓脇丼傲刹哺喩嗅嘲毀彙恣惧慄憬拉摯曖楷鬱璧瘍箋籠緻羞訃諧貪踪辣錮'),
    (u'Jinmeiyou (regular)', u'丑丞乃之乎也云亘亙些亦亥亨亮仔伊伍伽佃佑伶侃侑俄俠俣俐倭俱倦倖偲傭儲允兎兜其冴凌凜凛凧凪凰凱函劉劫勁勿匡廿卜卯卿厨厩叉叡叢叶只吾吞吻哉啄哩喬喧喰喋嘩嘉嘗噌噂圃圭坐尭堯坦埴堰堺堵塙塡壕壬夷奄奎套娃姪姥娩嬉孟宏宋宕宥寅寓寵尖尤屑峨峻崚嵯嵩嶺巌巖已巳巴巷巽帖幌幡庄庇庚庵廟廻弘弛彌彗彦彪彬徠忽怜恢恰恕悌惟惚悉惇惹惺惣慧憐戊或戟托按挺挽掬捲捷捺捧掠揃摑摺撒撰撞播撫擢孜敦斐斡斧斯於旭昂昊昏昌昴晏晃晄晒晋晟晦晨智暉暢曙曝曳曾朋朔杏杖杜李杭杵杷枇柑柴柘柊柏柾柚桧檜栞桔桂栖桐栗梧梓梢梛梯桶梶椛梁棲椋椀楯楚楕椿楠楓椰楢楊榎樺榊榛槙槇槍槌樫槻樟樋橘樽橙檎檀櫂櫛櫓欣欽歎此殆毅毘毬汀汝汐汲沌沓沫洸洲洵洛浩浬淵淳渚淀淋渥湘湊湛溢滉溜漱漕漣澪濡瀕灘灸灼烏焰焚煌煤煉熙燕燎燦燭燿爾牒牟牡牽犀狼猪獅玖珂珈珊珀玲琢琉瑛琥琶琵琳瑚瑞瑶瑳瓜瓢甥甫畠畢疋疏瘦皐皓眸瞥矩砦砥砧硯碓碗碩碧磐磯祇祢禰祐禄祿禎禱禽禾秦秤稀稔稟稜穣穰穿窄窪窺竣竪竺竿笈笹笙笠筈筑箕箔篇篠簞簾籾粥粟糊紘紗紐絃紬絆絢綺綜綴緋綾綸縞徽繫繡纂纏羚翔翠耀而耶耽聡肇肋肴胤胡脩腔膏臥舜舵芥芹芭芙芦苑茄苔苺茅茉茸茜莞荻莫莉菅菫菖萄菩萌萠萊菱葦葵萱葺萩董葡蓑蒔蒐蒼蒲蒙蓉蓮蔭蔣蔦蓬蔓蕎蕨蕉蕃蕪薙蕾蕗藁薩蘇蘭蝦蝶螺蟬蟹蠟衿袈袴裡裟裳襖訊訣註詢詫誼諏諄諒謂諺讃豹貰賑赳跨蹄蹟輔輯輿轟辰辻迂迄辿迪迦這逞逗逢遥遙遁遼邑祁郁鄭酉醇醐醍醬釉釘釧鋒鋸錐錆錫鍬鎧閃閏閤阿陀隈隼雀雁雛雫霞靖鞄鞍鞘鞠鞭頁頌頗頰顚颯饗馨馴馳駕駿驍魁魯鮎鯉鯛鰯鱒鱗鳩鳶鳳鴨鴻鵜鵬鷗鷲鷺鷹麒麟麿黎黛鼎'),
    (u'Jinmeiyou (variant)', u'亞惡爲衞谒緣應櫻奧橫溫價祸壞懷樂渴卷陷寬氣僞戲虛峽狹曉勳薰惠揭鷄藝擊縣儉劍險圈檢顯驗嚴廣恆黃國黑碎雜兒濕壽收從澁獸縱緖敍將涉燒獎條狀乘淨剩疊孃讓釀眞寢愼盡粹醉穗瀨齊靜攝專戰纖禪壯爭莊搜巢裝騷增藏臟卽帶滯單團彈晝鑄廳徵聽鎭轉傳燈盜稻德拜賣髮拔晚祕拂佛步飜每默藥與搖樣謠來賴覽龍綠淚壘曆歷鍊郞錄')
    ]
_kanken = [
    (u'Unlisted', ''),
    (u'Level 10',    u'一右雨円王音下火花貝学気休玉金九空月犬見五口校左三山四子糸字耳七車手十出女小上森人水正生青石赤先千川早草足村大男竹中虫町天田土二日入年白八百文本名木目夕立力林六'),
    (u'Level 9',     u'引羽雲園遠黄何夏家科歌画会回海絵外角楽活間丸岩顔帰汽記弓牛魚京強教近兄形計元原言古戸午後語交光公工広考行高合国黒今才細作算姉市思止紙寺時自室社弱首秋週春書少場色食心新親図数星晴声西切雪線船前組走多太体台谷知地池茶昼朝長鳥直通弟店点電冬刀東当答頭同道読内南肉馬買売麦半番父風分聞米歩母方北妹毎万明鳴毛門夜野矢友曜用来理里話'),
    (u'Level 8',     u'悪安暗委意医育員飲院運泳駅央横屋温化荷界開階寒感漢館岸期起客宮急球究級去橋業局曲銀区苦具君係軽決血研県庫湖向幸港号根祭坂皿仕使始指死詩歯事持次式実写者主取守酒受州拾終習集住重宿所暑助勝商昭消章乗植深申真神身進世整昔全想相送息速族他打対待代第題炭短談着柱注丁帳調追定庭笛鉄転登都度島投湯等豆動童農波配倍箱畑発反板悲皮美鼻筆氷表病秒品負部服福物平返勉放味命面問役薬油有由遊予様洋羊葉陽落流旅両緑礼列練路和'),
    (u'Level 7',     u'愛案以位囲胃衣印栄英塩億加果課貨芽改械害街各覚完官管観関願喜器希旗機季紀議救求泣給挙漁競共協鏡極訓軍郡型径景芸欠結健建験固候功好康航告差最菜材昨刷察札殺参散産残司史士氏試児治辞失借種周祝順初唱松焼照省笑象賞信臣成清静席積折節説戦浅選然倉巣争側束続卒孫帯隊達単置仲貯兆腸低停底的典伝徒努灯働堂得特毒熱念敗梅博飯費飛必標票不付夫府副粉兵別変辺便包法望牧末満未脈民無約勇要養浴利陸料良量輪類令例冷歴連労老録'),
    (u'Level 6',     u'圧易移因営永衛液益演往応恩仮価可河過賀解快格確額刊幹慣眼基寄規技義逆久旧居許境興均禁句群経潔件券検険減現限個故護効厚構耕講鉱混査再妻採災際在罪財桜雑賛酸師志支枝資飼似示識質舎謝授修術述準序承招証常情条状織職制勢性政精製税績責接設絶舌銭祖素総像増造則測属損態貸退団断築張提程敵適統導銅徳独任燃能破判版犯比肥非備俵評貧婦富布武復複仏編弁保墓報豊暴貿防務夢迷綿輸余預容率略留領'),
    (u'Level 5',     u'異遺域宇映延沿我灰拡閣革割株巻干看簡危揮机貴疑吸供胸郷勤筋敬系警劇激穴憲権絹厳源呼己誤后孝皇紅鋼降刻穀骨困砂座済裁策冊蚕姿私至視詞誌磁射捨尺若樹収宗就衆従縦縮熟純処署諸除傷将障城蒸針仁垂推寸盛聖誠宣専泉洗染善創奏層操窓装臓蔵存尊宅担探誕暖段値宙忠著庁潮頂賃痛展党糖討届難乳認納脳派俳拝背肺班晩否批秘腹奮並閉陛片補暮宝訪亡忘棒枚幕密盟模訳優郵幼欲翌乱卵覧裏律臨朗論'),
    (u'Level 4',     u'握扱依偉威為維緯違井壱稲芋陰隠影鋭越援煙縁鉛汚奥押沖憶暇箇菓雅介壊戒皆獲較刈乾勧歓汗環甘監鑑含奇幾祈輝鬼儀戯詰却脚丘及朽巨拠距凶叫恐況狂狭響驚仰駆屈掘繰傾恵継迎撃兼剣圏堅肩軒遣玄枯誇鼓互御恒抗攻更稿荒項香豪腰込婚鎖彩歳載剤咲惨伺刺旨紫脂雌執芝斜煮釈寂朱狩趣需秀舟襲柔獣瞬旬盾巡召床沼称紹詳丈畳飾殖触侵寝慎振浸薪震尋尽陣吹澄是姓征跡占扇鮮訴僧燥騒贈即俗耐替拓沢濁脱丹嘆淡端弾恥致遅蓄徴跳沈珍堤抵摘滴添殿吐渡途奴怒倒唐塔桃盗到踏逃透闘胴峠突曇鈍弐悩濃杯輩拍泊薄迫爆髪罰抜搬繁般販範盤彼疲被避尾微匹描浜敏怖敷普浮腐膚賦舞幅払噴柄壁舗捕峰抱砲傍坊帽忙冒肪凡盆慢漫妙眠矛霧娘茂猛網黙紋躍雄与誉溶謡踊翼頼雷絡欄離粒慮療隣涙隷麗齢暦劣烈恋露郎惑腕'),
    (u'Level 3',     u'哀慰詠悦閲宴炎欧殴乙卸穏佳嫁架華餓塊怪悔慨概該穫郭隔岳掛滑冠勘喚換敢緩肝貫企岐忌既棋棄軌騎欺犠菊吉喫虐虚峡脅凝斤緊愚偶遇桑刑啓契憩掲携鶏鯨倹賢幻孤弧雇顧娯悟坑孔巧慌拘控甲硬絞綱郊酵克獄墾恨紺魂債催削搾錯撮擦暫施祉諮侍慈軸湿疾赦邪殊寿潤遵徐匠掌昇晶焦衝鐘冗嬢譲錠嘱辱伸審辛炊粋衰遂酔随髄瀬牲請隻惜斥籍摂潜繕措礎粗阻双掃葬遭憎促賊怠滞胎袋逮滝卓択託諾奪胆鍛壇稚畜窒抽鋳駐彫聴超鎮陳墜帝締訂哲塗斗凍痘陶匿篤豚如尿粘婆排陪縛伐伴帆畔藩蛮卑泌碑姫漂苗符赴封伏覆墳紛癖穂募慕簿倣奉崩縫胞芳邦飽乏妨房某膨謀墨没翻魔埋膜又魅婿滅免幽憂誘揚揺擁抑裸濫吏隆了猟糧陵厘励零霊裂廉錬炉廊楼浪漏湾'),
    (u'Level Pre-2', u'亜尉逸姻韻渦浦疫謁猿凹翁寡禍稼蚊懐拐劾涯垣嚇核殻潟喝括渇褐轄且堪寛患憾棺款缶艦還閑陥頑飢偽宜擬窮糾拒享恭挟矯暁琴菌襟謹吟虞隅靴勲薫慶渓茎蛍傑嫌懸献謙顕弦呉碁侯江洪溝肯衡貢購剛拷酷懇昆佐唆詐宰栽砕斎崎索傘桟嗣肢賜滋璽漆遮蛇爵酌珠儒囚愁臭酬醜充汁渋銃叔淑粛塾俊准循殉庶緒叙償升奨宵尚彰抄渉症硝礁祥粧肖訟詔剰壌浄醸唇娠紳診刃甚迅酢帥睡崇枢据杉畝誓逝斉析拙窃仙栓旋繊薦践遷漸禅塑疎租喪壮捜挿曹槽荘藻霜堕妥惰駄泰濯但棚痴逐秩嫡衷弔懲挑眺勅朕津塚漬坪釣亭偵貞呈廷艇逓邸泥徹撤迭悼搭棟筒謄騰洞督凸屯縄軟尼妊忍寧猫把覇廃培媒賠伯舶漠肌鉢閥煩頒妃扉披罷賓頻瓶扶譜附侮沸憤雰丙併塀幣弊偏遍俸泡褒剖紡僕撲朴堀奔摩磨麻抹繭岬銘妄盲耗戻厄柳愉癒諭唯悠猶裕融庸窯羅酪履痢硫竜虜僚寮涼倫塁累鈴賄枠'),
    (u'Level 2',     u'挨宛闇椅畏萎茨咽淫臼唄餌怨艶旺岡臆俺苛牙崖蓋骸柿顎葛釜鎌瓦韓玩伎畿亀僅巾錦駒串窟熊稽詣隙桁拳鍵舷股虎乞勾喉梗頃痕沙挫塞采阪埼柵拶斬鹿叱嫉腫呪蹴拭尻芯腎須裾凄醒戚脊煎羨腺詮膳曽狙遡爽痩捉袖遜汰唾堆戴誰旦綻酎捗椎潰爪鶴諦溺填貼妬賭藤憧瞳栃頓奈那謎鍋匂虹捻罵剥箸斑氾汎眉膝肘媛阜蔽蔑蜂貌頬睦勃昧枕蜜冥麺餅冶弥湧妖沃嵐藍梨璃侶瞭瑠呂賂弄麓脇丼傲刹哺喩嗅嘲毀彙恣惧慄憬拉摯曖楷鬱璧瘍箋籠緻羞訃諧貪踪辣錮'),
    (u'Level Pre-1', u'唖娃阿姶逢葵茜穐渥旭葦鯵梓斡姐虻飴絢綾鮎或粟袷庵按鞍杏伊夷惟謂亥郁磯溢鰯允胤蔭吋烏迂卯鵜窺丑碓嘘欝蔚鰻姥厩瓜閏噂云荏叡嬰曳洩瑛盈穎頴榎厭堰奄掩焔燕苑薗鴛於甥襖鴬鴎荻桶牡伽嘉珂禾茄蝦嘩迦霞俄峨臥蛾駕廻恢魁晦芥蟹凱咳碍鎧浬馨蛙蛎鈎劃廓撹赫笠樫橿梶鰍恰鰹叶椛樺鞄兜竃蒲噛鴨茅萱粥苅侃姦柑桓澗潅竿翰莞諌舘巌癌翫贋雁嬉毅稀徽妓祇蟻誼掬鞠吃桔橘砧杵黍仇汲灸笈渠鋸禦亨侠僑兇匡卿喬彊怯蕎饗尭桐粁欣欽禽芹衿倶狗玖矩躯駈喰寓櫛釧屑沓轡窪隈粂栗鍬卦袈祁圭珪慧桂畦繋罫荊頚戟訣倦喧捲牽硯鹸絃諺乎姑狐糊袴胡菰跨鈷伍吾梧檎瑚醐鯉佼倖垢宏巷庚弘昂晃杭浩糠紘肱腔膏砿閤鴻劫壕濠轟麹鵠漉甑忽惚狛此坤昏梱艮些叉嵯瑳裟坐哉犀砦冴堺榊肴鷺朔窄鮭笹匙薩皐鯖捌錆鮫晒撒燦珊纂讃餐仔屍孜斯獅爾痔而蒔汐鴫竺宍雫悉蔀篠偲柴屡蕊縞紗勺杓灼錫惹綬洲繍蒐輯酋什戎夙峻竣舜駿楯淳醇曙渚薯藷恕鋤哨嘗妾娼庄廠捷昌梢樟樵湘菖蒋蕉裳醤鉦鍾鞘丞擾杖穣埴燭蝕晋榛疹秦塵壬訊靭笥諏厨逗翠錐錘瑞嵩趨雛椙菅頗雀摺棲栖脆蹟碩蝉尖撰栴煽穿箭舛賎銑閃糎噌岨曾楚疏蘇鼠叢宋匝惣掻槍漕糟綜聡蒼鎗其揃詑柁舵楕陀騨岱腿苔黛鯛醍鷹啄托琢鐸茸凧蛸只叩辰巽竪辿狸鱈樽坦歎湛箪耽蛋檀弛智蜘馳筑註樗瀦猪苧凋喋寵帖暢牒脹蝶諜銚槌鎚栂掴槻佃柘辻蔦綴鍔椿壷嬬紬吊剃悌挺梯汀碇禎蹄鄭釘鼎擢鏑轍纏甜顛澱兎堵杜菟鍍砥砺塘套宕嶋梼淘涛祷董蕩鐙撞萄鴇涜禿橡椴鳶苫寅酉瀞噸惇敦沌遁呑乍凪薙灘捺楢馴畷楠汝賑廿韮濡禰祢葱撚乃廼之埜嚢膿覗蚤巴播杷琶芭盃牌楳煤狽這蝿秤矧萩柏箔粕曝莫駁函硲肇筈幡畠溌醗筏鳩噺塙蛤隼叛挽磐蕃匪庇斐緋誹樋簸枇毘琵柊稗疋髭彦菱弼畢逼桧紐謬彪瓢豹廟錨鋲蒜蛭鰭彬斌瀕埠斧芙撫葡蕪楓葺蕗淵弗鮒吻扮焚糞頁僻碧瞥箆篇娩鞭鋪圃甫輔戊菩呆峯庖捧朋烹萌蓬鋒鳳鵬鉾吠卜穆釦殆幌哩槙鮪柾鱒亦俣沫迄麿蔓巳箕湊蓑稔粍牟鵡椋姪牝棉緬摸孟蒙儲杢勿尤籾貰悶匁也爺耶靖薮鑓愈佑宥揖柚涌猷祐邑輿傭楊熔耀蓉遥慾淀螺莱洛蘭李裡葎掠劉溜琉亮凌梁稜諒遼淋燐琳鱗麟伶嶺怜玲苓憐漣煉簾聯蓮魯櫓婁牢狼篭聾蝋禄肋倭歪鷲亙亘鰐詫藁蕨椀碗儘兔凰凾厰咒壺峩崕嵳廐廏廚攪檜檮橢濤渕溯漑灌潴皋礦礪龝竈篦纒翆聨苒莵萠葢蘂蕋藪蠣蛛蠅蠏諫讚豎賤迺鉤鎔靱韃韭頸鰺鴈鴦鶯鸚麒麪'),
    (u'Level 1',     u'芦讐屠櫨桝榔弌丐丕丱乂乖舒弍于亟亢亶仍仄仆仂仗仞仭仟价伉佚估佝佗佇佶侈侏侘佻佩佰侑佯侖俔俟俎俘俛俑俚俐俤俥倚倨倔倪倥倅伜俶倡倩倬俾俯們倆偃偕偐偈做偖偬偸傀傚傅傴僉僊僂僖僥僭僣僮僵儁儂儕儔儚儡儺儷儼儻兀兌兢兪冀冉冏冑冓冕冤冢冪冱冽凅凛几凩凭刋刔刎刪刮刳剏剄剋剌剞剔剪剴剳剿剽劈劬劭劼勁勍勗勣勦飭勠匆匈甸匍匐匏匕匚匣匯匱匳卅丗卉卍卮卻厖厥簒叟曼燮叮叨叭叺吁吽听吭吼吮吶吩吝呎咏呵咎呟呱呷呰呻咀呶咄咐咆哇咢咸咥咬哄哈咨咫哂咤哥哦唏唔哽哮哭哢唹啀啣售啜啅啖啗唸唳喙喀喊喟啻啾喘喞啼喃喇喨嗚嗟嗄嗜嗤嗔嘔嗷嘖嗾嗽嘛噎嘴嘶嘸噫噤嘯噬噪嚆嚀嚊嚠嚔嚏嚥嚮嚶囂嚼囁囃囀囈囓囮囹圀囿圄圉嗇圜圦坎圻址坏坩坡坿垓垠垤埃埆埒堊堋堙堝堡塋塒堽塹墅墟壅壑壙壜壟壼夐夥夬夭夲夸夾奕奐奎奚奘奢奠奸妁妝佞妣妲姆姨姜妍姙姚娥娟娑娜娉婀婬婉娵娶婢婪媚媼媾嫋嫂媽嫣嫗嫦嫩嫖嫺嫻嬌嬋嬖嬲嬪嬶嬾孅孀孑孕孚孛孥孩孰孳孵孺宦宸寇寔寐寤寞寥寰尠尨尸尹屁屎屓屏孱屮屶屹岌岑岔岫峙峭峪崟崛崑崔崢崚崙崘嵌嵒嵎嵋嵬嶇嶄嶂嶢嶝嶬嶮嶷嶼巉巍巓巒巫已帚帙帑帛帷幄幃幀幎幗幔幟幢幇幵并幺麼庠廁廂廈廖廝廛廡廨廩廬廱廸彝彜弋弑弖弩弭弸彎弯彗彭彷徂彿徊很徇徙徘徨徭徼忖忻忤忸忱忝忿怡怙怩怎怱怛怕怫怦怏怺恚恁恪恟恊恍恃恤恂恬恫恙悁悍悃悚悄悛悖悒悧悋悸惓悴忰悽惆悵惘慍愕愆惶惷愀惴惺愃惻愍愎慇愾愨愧慊愿愬愴慂慳慷慙慚慫慴慥慟慝慓慵憖憔憚憊憑憫憮懌懊懈懃懆憺懋罹懍懦懣懶懺懴懿懽懼懾戈戉戍戌戔戛戞戡截戮戳扁扎扞扣扛扠扨扼抉找抒抓抖抃抔拗拑抻拏拿拆拈拌拊拇抛挌拮拱挂挈拯拵捐捍捏掖掎掀掫捶掣掏掉掟捫捩掾揩揀揆揣揉揶揄搴搆搓搦搶搗搨搏摧摶摎撕撓撥撩撈撼擒擅撻擘擂擱擠擡抬擣擯攬擲擺攀擽攘攅攤攣攫畋敖敞敝敲斂斃斛斟斫旃旆旁旄旌旒旛旱杲昊昃旻杳昵昶昴昜晏晁晞晤晧晨晟晢晰暈暉暄暘暝曁暹暾曄曚曠昿曦曩曰曷朏朞朦朧霸朮朿朶朸杆杞杠杙杣枉杼杪枌枋枡枅枷柯枴柬枳柩枸柤柞柝柢柮枹柎栞框栩桀栲桎梳栫桙桷桿梟梏梭梔梛梃桴梵梠梺椏椁棊椈棘棍棕椶椒椄棗棣棹棠椚楹楸楫楔楾楮椹椽椰楡楞楝榁槐槁槓榾槎寨槊榻槃榧榑榜榕榴槨樛槿槹槲槧樅榱槭樔樊樒櫁橄橇橙橦橈樸檐檠檄檣檗蘗檻櫃櫂檸檳檬櫟檪櫚櫪欅蘖櫺欒欖欸欷欹歇歃歉歙歔歛歟歿殀殄殃殍殞殤殪殫殯殲殱殷毋毟毬毫毳毯麾氈氓氛汞汕汪沂沍沚沁沛汨沐泄泓沽泗泅泝沮沱沾泛泯泪洟衍洶洫洽洸洵洒洌浣涓浚浹浙涎涕涅淹涵涸淆淬淌淒淅淙淤淪渭湮渙湲渾渣湫渫湍渟湃渺湎渝游溂溘滉溷滓溽滄溲滔滕溏溥滂溟滬滸滾漿滲漱漲滌漾漓滷澆潺潸潯潭澂潘澎潦澳澣澡澹濆澪濬濔濘濛瀉瀋濺瀑瀁瀏濾瀛瀚瀝瀟瀰瀾瀲灑炙炒炯烱炬炸炳炮烟烋烝烙焉烽焜焙煥煕熈煦煢煌煖煬熏燻熄熕熨熬燗熹熾燉燔燎燠燬燧燵燼燹燿爍爛爨爬爰牀牆牋牘牴牾犂犁犇犒犖犢犲狃狆狄狎狒狢狠狡狷倏猗猊猜猖猝猴猯猩猥猾獏獗獪獰獺珈玳玻珀珥珮珞璢琅瑯琥琲琺瑕瑟瑙瑁瑜瑩瑰瑣瑪瑶瑾璋璞瓊瓏瓔瓠瓣瓧瓩瓮瓲瓰瓱瓸瓷甄甃甅甌甎甍甕甓甦畛畚畤畭畸疆疇畴疔疚疝疥疣痂疳痃疵疽疸疼疱痍痊痒痙痣痞痾痿痼瘁痰痺痲痳瘋瘉瘟瘧瘠瘡瘢瘤瘴瘰瘻癇癈癆癜癘癢癨癩癪癧癬癰癲癸皎皖皓皙皚皰皴皸皹皺盂盍盒盞盥盧盪蘯盻眈眇眄眩眥眦眛眷眸睇睚睨睫睛睥睾睹瞎瞋瞑瞠瞞瞰瞿瞼瞽瞻矇矍矚矜矣矮矼砌砒砠硅硼碚碌碣碪磑磋磔碾碼磅磊磬磧磚磽磴礒礑礙礬礫祀祠祗祟祚祓祺禊禝禧禳禹禺秉秕秧秬秣稈稍稠稟禀稷穡穢穹穽窈窕窘窖窩窶竅竄窿邃竇竍竏竕竓站竚竡竢竦竭竰笏笊笆笳笘笙笞笵笨筐筺笄筍笋筌筅筵筥筴筧筰筱筬筮箝箘箍箜箚箒箏筝箙篋篁篌箴篆篝篩簑簔篥簀簇簓篳篷簗簍簣簧簪簟簷簫簽籌籃籔籀籐籟籤籖籥籬籵粃粤粢粨粳粲粱粽糀糅糂糒糜鬻糯糲糴糶糺紆紂紜紕紊絅紮紲紿紵絆絳絖絎絨絮絏絣綉絛綏絽綛綺綮綣綵緇綽綫綢綯綸綟綰緘緝緤緞緲緡縅縊縡縒縟縉縋縢繆繦縻縵縹繃縷縲縺繧繝繖繞繙繚繹繻纃繽辮纈纉纐纓纔纛纜缸罅罌罍罎罕罔罘罟罠罨罩罧羂羆羃羈羇羌羔羝羚羯羲羹羶羸翅翊翕翔翡翦翩翳翹飜耆耄耋耘耙耜耡耨耿聊聆聒聘聚聟聢聳聶聿肄肆肛肓肚肭肬胛胥胙胝胄胚胖胯胱脛脩脣脯腋隋腆脾腓腑胼腱腮腥腴膃膈膊膀膂膠膣腟膩膰膵膾臀臂膺臉臍臑臙臘臚臠臧臻臾舁舂舅舐舫舸舳艀艙艘艝艚艟艤艢艨艪艫艱艸艾芍芒芫芟芻芬苡苣苟苴苳苺莓范苻苹苞茆苜茉苙茵茴茲茱荀茹荐荅茯茫茗茘莅莚莪莟莢茣莎荼荳荵莠莉莨菴菫菎菽萃菘菁菠菲萍萢莽萸葭萼葷蒭蒂葩葆葯萵蒹蒿蒟蓙蓍蒻蓐蓁蓆蓖蒡蔡蓿蓴蔗蔘蔬蔟蔕蔔蓼蕀蕣蕘蕈蕁蕕薀薤薈薑薊薨蕭薔薛薇薜蕷蕾薐藉薺薹藐藕藜藹蘊蘋藾藺蘆蘢蘚蘿虔虧虱蚓蚣蚩蚪蚋蚌蚶蚯蛄蛆蚰蛉蚫蛔蛞蛩蛬蛟蛯蜒蜆蜈蜀蜃蛻蜑蜉蜍蛹蜊蜴蜿蜷蜻蜥蜩蜚蝠蝟蝸蝌蝎蝴蝗蝨蝮蝙蝓蝣螟螂螯蟋螽蟀雖螫蟄螳蟇蟆螻蟯蟠蠍蟾蟶蟷蠎蟒蠑蠖蠕蠢蠡蠱蠹蠧衄衒衙衢衫衾袞衵衽袵衲袂袗袒袙袢袍袤袰袿袱裃裄裔裘裙裹褂裼裴裨裲褄褌褊褓褞褥褪褫襁襄褻褶褸襌褝襠襞襦襤襭襪襯襴襷覃覈覓覘覡覩覦覬覯覲覿觚觜觝觴訖訐訌訛訝訥訶詁詛詒詆詈詼詭詬詢誅誂誄誨誡誑誥誦誚誣諄諍諂諚諳諤諱謔諠諢諷諞諛謌謇謚諡謖謐謗謳鞫謦謫謾謨譁譌譏譎譖譛譚譫譟譬譴讌讎讒讖讙谺豁谿豈豌豕豢豺貂貉貊貎貘貽貲貶賈賁賚賽賺賻贄贅贇贏贍贐齎贓贔贖赧赭赳趁趙跂趾趺跏跚跖跌跛跋跪跫跟跣跼踉跿踝踞踟蹂踵踰踴蹊蹇蹉蹌蹐蹈蹙蹤蹠蹣蹕蹶蹲蹼躁躇躅躄躋躊躓躑躔躙躪躡躬躱躾軈軋軛軼軻軫軾輊輅輒輙輓輜輟輛輌輦輳輻輹轅轂輾轌轆轎轗轜轢轤辜辟辷迚迥迢迪邇迴逅迹逑逕逡逍逞逖逋逧逵迸遏遐遑遒逎遉逾遖遘遨遯遶邂遽邁邀邏邨邯邱邵郢郤扈郛鄂鄒鄙鄲酊酖酣酥酩酳酲醋醂醢醯醪醵醴醺釁釉釐釵鈞釿鈔鈕鈑鉞鉗鉅鉉鉈銕鈿鉋銜銖銓銛鋏銹銷鋩錏鋺錙錚錣錺錵錻鍠鍼鍮鎰鎬鎹鏖鏗鏨鏘鏃鏝鏐鏈鏤鐚鐔鐓鐃鐐鐶鐫鐺鑒鑠鑢鑞鑪鑰鑵鑷鑽鑚鑼鑾钁鑿閂閊閔閘閙閨閧閭閼閻閹閾闊濶闃闍闌闕闔闖闡闥闢阡阨阮阯陂陌陋陜陞陝陟陲陬隍隘隕隗隧隰隴雎雋雉雍襍雜霍雕雹霄霆霈霓霎霑霏霖霙霤霪霰霹霽霾靄靆靂靉靠靤靦靨勒靫鞅靼鞁靺鞆鞋鞏鞐鞜鞨鞦鞣鞳鞴韆韈韋韜齏韲竟韶韵頏頌頤頡頷頽顆顋顫顰顱顴顳颪颯颱颶飄飆飩飫餃餉餒餔餡餞餤餬餮餽餾饂饉饅饐饋饑饒饌饕馗馘馥馭馮駟駛駝駘駑駭駮駱駻駸騁騏騅駢騙騫驂驀驃騾驕驍驟驢驥驤驩驪骭骰骼髀髏髑髢髣髦髯髫髴髱髷髻鬆鬘鬚鬟鬢鬣鬧鬨鬩鬮鬯魄魃魏魍魎魑魘魴鮓鮃鮑鮖鮗鮟鮠鮨鮴鯀鯊鮹鯏鯑鯒鯣鯢鯤鯔鯡鯲鯱鯰鰕鰔鰉鰓鰌鰆鰈鰒鰊鰄鰮鰛鰥鰤鰰鱇鱆鰾鱚鱠鱧鱶鱸鳧鳬鳰鴉鴃鴆鴪鴣鴟鵄鴕鴒鵁鴿鴾鵆鵝鵞鵤鵑鵙鵲鶉鶇鶫鵯鵺鶚鶤鶩鶲鷁鶻鶸鶺鷆鷂鷙鷓鷸鷦鷭鷯鷽鸛鸞鹵鹹麁麈麋麌麕麑麝麩麸麭靡黌黎黏黐黔黜黝黠黥黯黴黶黷黹黻黼黽鼇鼈鼕鼬鼾齔齣齟齠齦齧齬齪齷齲齶龕龠凜熙')
    ]
_css = "body { background: #ccc url(/img/noise.png); }" + \
    ".info-wrapper { height: auto; width: 500px; margin: 4em auto; padding: 0 0 2em 0; position: relative; }" + \
    ".info { max-height: 120px; height: auto; padding: .5em 0; border-bottom: solid 1px #fff; border-radius: 0 0 1em 1em;" + \
    "	overflow: hidden; position: relative; transition: 1s; } p { margin: 1em; }" + \
    ".info:after, .aftershadow { bottom: 0; width: 100%; height: 3em; border-radius: 0 0 1em 1em; position: absolute;" + \
    "	background: linear-gradient(rgba(192,192,192,0), #ccc); content: ''; }" + \
    ".aftershadow { filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#00cccccc, endColorstr=#ffcccccc); }" + \
    ".info-wrapper input[type=checkbox] { display: none; } .info-wrapper label { left: 50%; bottom: 1.5em; width: 9em;" + \
    "	height: 1.25em; margin:  0 0 0 -4.5em; border-bottom: solid 1px #fff; border-radius: 0 0 1em 1em; overflow: hidden;" + \
    "	position: absolute; font: 700 .67em/1.25em Arial; text-align: center; text-shadow: 0 1px 0 #fff; cursor: pointer; }" + \
    ".info-wrapper label .more { margin: -.1em 0 .35em; transition: 1s; } .info-wrapper .switch { width: 4em; display: inline-block; }" + \
    ".info-wrapper input[type=checkbox]:checked ~ .info { max-height: 15em; } .info-wrapper input[type=checkbox]:checked + label .more { margin-top: -1.65em; }"

class getPostsThread(QThread):
    add_post = pyqtSignal(str)
    
class TestedUnit:
    def __init__(self, value):
        self.idx = 0
        self.value = value
        self.avg_interval = 0.0
        self.due = 0.0
        self.odue = 0.0
        self.count = 0
        self.mod = 0

    def addDataFromCard(self, idx, card, timeNow):
        if card.type > 0:
            newTotal = (self.avg_interval * self.count) + card.ivl

            self.count += 1
            self.avg_interval = newTotal / self.count
        if card.type == 2:
            if card.due < self.due or self.due == 0:
                self.due = card.due

            if card.odue < self.odue or self.odue == 0:
                self.odue = card.odue
                self.mod = self.odue

        if idx < self.idx or self.idx == 0:
            self.idx = idx

def isKanji(unichar):
    try:
        return unicodedata.name(unichar).find('CJK UNIFIED IDEOGRAPH') >= 0
    except ValueError:
        # a control character
        return False

def scoreAdjust(score):
    score += 1
    return 1 - 1 / (score * score)

def addUnitData(units, unitKey, i, card, timeNow):
    validKey = _ignore.find(unitKey) == -1 and (not _kanjionly or isKanji(unitKey))
    if validKey:
        if unitKey not in units:
            unit = TestedUnit(unitKey)
            units[unitKey] = unit

        units[unitKey].addDataFromCard(i, card, timeNow)

def hsvrgbstr(h, s=0.8, v=0.9):
    i = int(h*6.0)
    f = (h*6.0) - i
    p = v*(1.0 - s)
    q = v*(1.0 - s*f)
    t = v*(1.0 - s*(1.0-f))
    i = i%6
    if i == 0: return "#%0.2X%0.2X%0.2X" % (int(v*256), int(t*256), int(p*256))
    if i == 1: return "#%0.2X%0.2X%0.2X" % (int(q*256), int(v*256), int(p*256))
    if i == 2: return "#%0.2X%0.2X%0.2X" % (int(p*256), int(v*256), int(t*256))
    if i == 3: return "#%0.2X%0.2X%0.2X" % (int(p*256), int(q*256), int(v*256))
    if i == 4: return "#%0.2X%0.2X%0.2X" % (int(t*256), int(p*256), int(v*256))
    if i == 5: return "#%0.2X%0.2X%0.2X" % (int(v*256), int(p*256), int(q*256))


class KanjiGridAuto:
    def __init__(self, mw):
        self.wv = AnkiWebView()
        if mw:
            self.menuAction = QAction("Generate Kanji Grid (Automatic)", mw)
            self.menuAction2 = QAction("Kanji Grid Settings", mw)
            qconnect(self.menuAction.triggered, self.setup)
            qconnect(self.menuAction2.triggered, self.settings)
            #mw.connect(self.menuAction, SIGNAL("triggered()"), self.setup)
            #mw.connect(self.menuAction2, SIGNAL("triggered()"), self.settings)
            mw.form.menuTools.addSeparator()
            mw.form.menuTools.addAction(self.menuAction)
            mw.form.menuTools.addAction(self.menuAction2)
    
    def settings(self):
        global _suppressSettingsPopup
        global _pattern, _literal, _caseSensitive
        global _interval, _kanjiPerRowPNG, _chosenSortingMethods
        global _makePNG, _makeHTML, _printNotInDeck, _saveKanjiNotInDeck, _onlySeenKanji
        swin = QDialog(mw) #Settings window
        vl = QVBoxLayout() #The layout of the window
        frm = QGroupBox("Settings") #Make a group
        vl.addWidget(frm) #Add it to the main layout
        il = QVBoxLayout() 
        fl = QHBoxLayout()
        fl2 = QHBoxLayout()
        fl3 = QHBoxLayout()
        
        #--------------------------------
        
        field = QLineEdit()
        searchTerms = ", ".join(_pattern)
        field.setText(searchTerms)
        il.addWidget(QLabel("Names of fields to search:\nTo ensure correct output, try to use \"Match exactly\". \nMatched fields with no kanji will drop the entire card and not include the kanji in the grid even if its other fields contain kanji."))
        fl.addWidget(field)
        
        #--------------------------------
        
        liter = QCheckBox("Match exactly")
        liter.setChecked(_literal)
        fl.addWidget(liter)
        
        cs = QCheckBox("Case sensitive")
        cs.setChecked(_caseSensitive)
        fl.addWidget(cs)
        il.addLayout(fl)
        
        #--------------------------------
        
        oskan = QCheckBox("Only analyze kanji you've seen (affects other settings)")
        oskan.setChecked(_onlySeenKanji)
        fl2.addWidget(oskan)
        
        iknid = QCheckBox("Also include kanji not in decks")
        iknid.setChecked(_printNotInDeck)
        fl2.addWidget(iknid)

        il.addLayout(fl2)
        il.addWidget(QLabel(""))
        
        #--------------------------------
        
        jlpt = QCheckBox("JLPT")
        jlpt.setChecked("jlpt" in _chosenSortingMethods)
        fl3.addWidget(jlpt)
        
        jouyou = QCheckBox("Jouyou")
        jouyou.setChecked("jouyou" in _chosenSortingMethods)
        fl3.addWidget(jouyou)
        
        kanken = QCheckBox("Kanken")
        kanken.setChecked("kanken" in _chosenSortingMethods)
        fl3.addWidget(kanken)
        
        order_found = QCheckBox("Order found")
        order_found.setChecked("order found" in _chosenSortingMethods)
        fl3.addWidget(order_found)
        
        unicode = QCheckBox("Unicode")
        unicode.setChecked("unicode" in _chosenSortingMethods)
        fl3.addWidget(unicode)
        
        score = QCheckBox("Score")
        score.setChecked("score" in _chosenSortingMethods)
        fl3.addWidget(score)
        
        frequency = QCheckBox("Frequency  |")
        frequency.setChecked("frequency" in _chosenSortingMethods)
        fl3.addWidget(frequency)
        
        popup = QCheckBox("Suppress popup on saved settings")
        popup.setChecked(_suppressSettingsPopup)
        fl3.addWidget(popup)
        il.addLayout(fl3)

        #--------------------------------
        
        stint = QSpinBox()
        stint.setRange(1,65536)
        stint.setValue(_interval)
        il.addWidget(QLabel("Card interval considered strong - Default 180"))
        il.addWidget(stint)
        
        pngcol = QSpinBox()
        pngcol.setRange(1,9999)
        pngcol.setValue(_kanjiPerRowPNG)
        il.addWidget(QLabel("Number of kanji per row in grid - Default 20"))
        il.addWidget(pngcol)
        
        savePath = QPushButton("Choose save location")
        il.addWidget(savePath)
        tempwin = QDialog(swin)
        savePath.clicked.connect(lambda:self.setBaseFilePath(tempwin))
        
        il.addWidget(QLabel("Saving to directory: " + _filePathBase))
        
        frm.setLayout(il)
        
        hl = QHBoxLayout()
        vl.addLayout(hl)
        sset = QPushButton("Save Settings")
        hl.addWidget(sset)
        sset.clicked.connect(swin.accept)
        
        cls = QPushButton("Close")
        hl.addWidget(cls)
        cls.clicked.connect(swin.reject)
        
        swin.setLayout(vl)
        swin.setTabOrder(sset,cls)
        swin.setTabOrder(cls,field)
        swin.setTabOrder(field,liter)
        swin.setTabOrder(liter,stint)
        swin.setTabOrder(stint,pngcol)
        swin.resize(400, 200)
        
        if swin.exec():
            if len(field.text().strip()) != 0: _pattern = field.text().replace(" ", "").split(",")
            _chosenSortingMethods = []
            _literal = liter.isChecked()
            _caseSensitive = cs.isChecked()
            # _makePNG = sPNG.isChecked()
            # _makeHTML = sHTML.isChecked()
            _printNotInDeck = iknid.isChecked()
            # _saveKanjiNotInDeck = sknid.isChecked()
            _onlySeenKanji = oskan.isChecked()
            _interval = stint.value()
            _kanjiPerRowPNG = pngcol.value()
            if jlpt.isChecked():        _chosenSortingMethods.append("jlpt")
            if jouyou.isChecked():      _chosenSortingMethods.append("jouyou")
            if kanken.isChecked():      _chosenSortingMethods.append("kanken")
            if order_found.isChecked(): _chosenSortingMethods.append("order found")
            if unicode.isChecked():     _chosenSortingMethods.append("unicode")
            if score.isChecked():       _chosenSortingMethods.append("score")
            if frequency.isChecked():   _chosenSortingMethods.append("frequency")
            if popup.isChecked():
                _suppressSettingsPopup = True
            else:
                _suppressSettingsPopup = False
                
            self.saveSettings()    
            if not _suppressSettingsPopup: 
                showInfo("Your settings have been saved for this session.\nThey will go back to default when Anki is restarted\n\n If you want these settings to be permanent, please paste the content of\n%s/Kanji_Grid_Settings.txt \ninto line 34 in Kanji_Grid_Automatic_Version.py\nYou find this file by clicking Tools -> Add-ons -> Open Add-ons Folder" % os.path.abspath(_filePathBase))
        return
    
    def saveSettings(self):
        global _filePathBase
        global _pattern, _literal, _caseSensitive
        global _interval, _kanjiPerRowPNG, _chosenSortingMethods
        global _makePNG, _makeHTML, _printNotInDeck, _saveKanjiNotInDeck, _onlySeenKanji
    
        fileName = os.path.join(_filePathBase, "Kanji_Grid_Settings.txt")
        
        #Trying to fix some Windows shit
        strPath = ""
        if ("\\" in _filePathBase): 
            strPath += _filePathBase.replace("\\", "\\\\")
        else:
            strPath += _filePathBase
            
        try:
            with open(fileName, 'w', encoding='utf-8') as f:
                s1 = "_pattern = [\"" + "\",\"".join(_pattern) + "\"]"
                s2 = "\n_literal = " + str(_literal)
                s3 = "\n_caseSensitive = " + str(_caseSensitive)
                s4 = "\n_filePathBase = \"" + strPath + "\""
                s5 = "\n_interval = " + str(_interval)
                s6 = "\n_kanjiPerRowPNG = " + str(_kanjiPerRowPNG)
                s7 = "\n_chosenSortingMethods = " + "[\"" + "\", \"".join(_chosenSortingMethods) + "\"]"
                s8 = "\n_printNotInDeck = " + str(_printNotInDeck)
                s12 = "\n_onlySeenKanji = " + str(_onlySeenKanji)
                s13 = "\n_suppressSettingsPopup = " + str(_suppressSettingsPopup)
                
                settingsString = s1+s2+s3+s4+s5+s6+s7+s8+s12+s13
                
                f.write(settingsString)
        except Exception as e:
            print("Error:", e)
        return       
        
    def setBaseFilePath(self, tempwin):
        global _filePathBase
        desktop_path = os.path.join(os.path.expanduser("~"), "Desktop")
        _filePathBase = QFileDialog.getExistingDirectory(tempwin, "Save Page", desktop_path)
            
    def generate(self, units, timeNow, autoModTime, filePath, saveMode=False):
        global _printNotInDeck
        currentTimeTemp = autoModTime.replace(":", "")
        currentTime = currentTimeTemp.replace(" ", "-")
        if saveMode: cols = _wide
        else: cols = _kanjiPerRowPNG
        self.html  = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n"
        self.html += "<html><head><title>Anki Kanji Grid</title></head><body bgcolor=\"#FFF\">\n"
        self.html += "<span style=\"font-size: 3em;color: #888;\">Kanji Grid - " + autoModTime + "</span><br>\n"
        self.html += "<div style=\"margin-bottom: 24pt;padding: 20pt;\"><p style=\"float: left\">Key:</p>"
        self.html += "<style type=\"text/css\">table {font-size:16px; font-family:meiryo;}" + \
        			 ".key{display:inline-block;width:3em;}a,a:visited{color:#000;text-decoration:none;}</style>"
        self.html += "<p style=\"float: right\">Weak&nbsp;"
        for c in [n/6.0 for n in range(6+1)]:
            self.html += "<span class=\"key\" style=\"background-color: %s;\">&nbsp;</span>" % hsvrgbstr(c/2)
        self.html += "&nbsp;Strong</p></div>\n"
        self.html += "<div style=\"clear: both;\"><br><hr style=\"border-style: dashed;border-color: #666;width: 60%;\"><br></div>\n"
        self.html += "<center>\n"
        if _group in (0, 1, 2):
            if _group == 0:
                _grades = _jlpt
            elif _group == 1:
                _grades = _jouyou
            elif _group == 2:
                _grades = _kanken
            gc = 0
            kanji = list([u.value for u in units.values()])
            kanjiNotInDeck = ""
            for i in range(1,len(_grades)):
                kanjiNotInDeck += (_grades[i][0] + ":\n")
                self.html += "<h2 style=\"color:#888;\">%s Kanji</h2>\n" % _grades[i][0]
                table = "<table width='85%'><tr>\n"
                count = -1
                for unit in [units[c] for c in _grades[i][1] if c in kanji]:
                    if unit.count != 0 or not _onlySeenKanji:
                        score = "NaN"
                        count += 1
                        if count % cols == 0 and count != 0: table += "</tr>\n<tr>\n"
                        if unit.count != 0: bgcolour = hsvrgbstr(scoreAdjust(unit.avg_interval / _interval)/2)
                        else: bgcolour = "#FFF"
                        if _tooltips:
                            tooltip  = "Character: %s | Count: %s | " % (unicodedata.name(unit.value), unit.count)
                            tooltip += "Avg Interval: %s | Score: %s | " % (unit.avg_interval, score)
                            tooltip += "Background: %s | Index: %s" % (bgcolour, count)
                            table += "\t<td align=center valign=top style=\"background:%s;\" title=\"%s\">" % (bgcolour, tooltip)
                        else: table += "\t<td align=center valign=top style=\"background:%s;\">" % (bgcolour)
                        table += "<a href=\"http://jisho.org/kanji/details/%s\">%s</a></td>\n" % (2*(unit.value,))
                table += "</tr></table>\n"
                n = count+1
                t = len(_grades[i][1])
                gc += n
                if not _onlySeenKanji:
                    detailsTag1 = "" if _printNotInDeck else "<details>"
                    detailsTag2 = "" if _printNotInDeck else "</details>"
                    table += (detailsTag1 + "<summary>Missing kanji</summary><table style=\"max-width:75%;\"><tr>\n")
                    count = -1
                    kanjiOnCurrentLine = 0
                    for char in [c for c in _grades[i][1] if c not in kanji]:
                        score = "NaN"
                        count += 1
                        if count % cols == 0: table += "</tr>\n<tr>\n"
                        if _tooltips:
                            tooltip  = "Character: %s" % (unicodedata.name(char))
                            table += "\t<td align=center valign=top style=\"background:#EEE;color:#FFF;\" title=\"%s\">" % (tooltip)
                        else: table += "\t<td align=center valign=top style=\"background:#EEE;color:#FFF;\">"
                        table += "<a href=\"http://jisho.org/kanji/details/%s\" style=\"color:#888;\">%s</a></td>\n" % (2*(char,))
                        kanjiNotInDeck += ("\n" + char) if (kanjiOnCurrentLine % _kanjiPerRowPNG) == 0 else char
                        kanjiOnCurrentLine += 1
                    if count == -1: table += "<strong style=\"color:#CCC\">None</strong>"
                    table += ("</tr></table>" + detailsTag2 + "\n") 
                self.html += "<h4 style=\"color:#888;\">%d of %d - %0.2f%% in deck</h4>\n" % (n, t, n*100.0/t)
                self.html += table
                kanjiNotInDeck += "\n\n-------------------------\n"
                
            """
            if _saveKanjiNotInDeck:
                self.saveKanjiNotInDeck(filePath, currentTime, kanjiNotInDeck)
            """
            
            chars = reduce(lambda x,y: x+y, dict(_grades).values())
            self.html += "<h2 style=\"color:#888;\">%s Kanji</h2>" % _grades[0][0]
            table = "<table width='85%'><tr>\n"
            count = -1
            for unit in [u for u in units.values() if u.value not in chars]:
                if unit.count != 0 or not _onlySeenKanji:
                    score = "NaN"
                    count += 1
                    if count % cols == 0 and count != 0: table += "</tr>\n<tr>\n"
                    if unit.count != 0: bgcolour = hsvrgbstr(scoreAdjust(unit.avg_interval / _interval)/2)
                    else: bgcolour = "#FFF"
                    if _tooltips:
                        tooltip  = "Character: %s | Count: %s | " % (unicodedata.name(unit.value), unit.count)
                        tooltip += "Avg Interval: %s | Score: %s | " % (unit.avg_interval, score)
                        tooltip += "Background: %s | Index: %s" % (bgcolour, count)
                        table += "\t<td align=center valign=top style=\"background:%s;\" title=\"%s\">" % (bgcolour, tooltip)
                    else: table += "\t<td align=center valign=top style=\"background:%s;\">" % (bgcolour)
                    table += "<a href=\"http://jisho.org/kanji/details/%s\">%s</a></td>\n" % (2*(unit.value,))
            table += "</tr></table>\n"
            n = count+1
            self.html += "<h4 style=\"color:#888;\">%d of %d - %0.2f%%</h4>\n" % (n, gc, 0 if gc == 0 else n*100.0/gc)
            self.html += table
        else:
            table = "<table width='85%'><tr>\n"
            if _group == 3: # Order found
                unitsList = sorted( units.values(), key=lambda unit: (unit.idx, unit.count) )
            if _group == 4: # Unicode index
                unitsList = sorted( units.values(), key=lambda unit: (unicodedata.name(unit.value), unit.count) )
            if _group == 5: # Character score
                unitsList = sorted( units.values(), key=lambda unit: (scoreAdjust(unit.avg_interval / _interval), unit.count), reverse=True)
            if _group == 6: # Deck frequency
                unitsList = sorted( units.values(), key=lambda unit: (unit.count, scoreAdjust(unit.avg_interval / _interval)), reverse=True)
            count = -1
            for unit in unitsList:
                if unit.count != 0 or not _onlySeenKanji:
                    score = "NaN"
                    count += 1
                    if count % cols == 0 and count != 0: table += "</tr>\n<tr>\n"
                    if unit.count != 0: bgcolour = hsvrgbstr(scoreAdjust(unit.avg_interval / _interval)/2)
                    else: bgcolour = "#FFF"
                    if _tooltips:
                        tooltip  = "Character: %s | Count: %s | " % (unicodedata.name(unit.value), unit.count)
                        tooltip += "Avg Interval: %s | Score: %s | " % (unit.avg_interval, score)
                        tooltip += "Background: %s | Index: %s" % (bgcolour, count)
                        table += "\t<td align=center valign=top style=\"background:%s;\" title=\"%s\">" % (bgcolour, tooltip)
                    else: table += "\t<td align=center valign=top style=\"background:%s;\">" % (bgcolour)
                    table += "<a href=\"http://jisho.org/kanji/details/%s\">%s</a></td>\n" % (2*(unit.value,))
            table += "</tr></table>\n"
            self.html += "<h4 style=\"color:#888;\">%d total unique kanji</h4>\n" % (count+1)
            self.html += table


    def displaygrid(self, units, timeNow, _group):
        global _makePNG, _makeHTML
        filePath = self.getFilePath()
        
        autoModTime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        currentTimeTemp = autoModTime.replace(":", "")
        currentTime = currentTimeTemp.replace(" ", "-")
        
        self.generate(units, timeNow, autoModTime, filePath)
        self.win = QDialog(mw)
        if _group in (0, 1, 2):
            if _group == 0:
                self.win.setWindowTitle("Kanji Grid - JLPT")
            elif _group == 1:
                self.win.setWindowTitle("Kanji Grid - Jouyou")
            elif _group == 2:
                self.win.setWindowTitle("Kanji Grid - Kanken")
        else:
            if _group == 3: # Order found
                self.win.setWindowTitle("Kanji Grid - Order Found")
            if _group == 4: # Unicode index
                self.win.setWindowTitle("Kanji Grid - Unicode")
            if _group == 5: # Character score
                self.win.setWindowTitle("Kanji Grid - Score")
            if _group == 6: # Deck frequency
                self.win.setWindowTitle("Kanji Grid - Frequency")
                
        layout = QVBoxLayout()
        
        self.wv = AnkiWebView()
        self.wv.stdHtml(self.html)
        layout.addWidget(self.wv)     
        
        # Button layout
        button_layout = QHBoxLayout()
        
        # Save HTML button
        save_html_button = QPushButton("Save HTML")
        save_html_button.clicked.connect(lambda: self.saveHTML(filePath, currentTime))
        button_layout.addWidget(save_html_button)
        
        # Save Image button
        save_image_button = QPushButton("Save Image")
        save_image_button.clicked.connect(lambda: self.savePNG(filePath, currentTime))
        button_layout.addWidget(save_image_button)
        
        # Add the button layout to the main layout
        layout.addLayout(button_layout)
        
        self.win.setLayout(layout)
        self.win.resize(600, 700)

        self.win.show()
        return 0


    
    """
    def saveKanjiNotInDeck(self, filePath, currentTime, kanjiList):
        fileName = os.path.join(filePath, (currentTime + ".txt"))
        f = open(fileName, "w")
        f.write(str(kanjiList.encode("utf-8")))
        f.close()
        return
    """
    
    def getFilePath(self):
        global _filePathBase
        
        folder = ""
        if _group == 0:
            folder = "Sorted_by_jlpt"
        elif _group == 1:
            folder = "Sorted_by_jouyou"
        elif _group == 2:
            folder = "Sorted_by_kanken"
        elif _group == 3:
            folder = "Sorted_by_order_found"
        elif _group == 4:
            folder = "Sorted_by_unicode"
        elif _group == 5:
            folder = "Sorted_by_score"
        elif _group == 6:
            folder = "Sorted_by_frequency_in_decks"
        
        
        filePath = os.path.join(_filePathBase, folder)
        
        if not os.path.exists(filePath):
            os.makedirs(filePath)
            
        return filePath
    
    def saveHTML(self, filePath, currentTime):
        fileName = os.path.join(filePath, (str(currentTime) + ".html"))
        with open(fileName, "w", encoding='utf-8') as f:
            f.write(self.html)
            f.close()
        return

    def savePNG(self, filePath, currentTime):
        # Get the size of the HTML content
        contentSize = self.wv.page().contentsSize().toSize()

        # Store the original size of the self.wv widget
        originalSize = self.wv.size()

        # Resize the self.wv widget to match the size of the HTML content
        self.wv.resize(contentSize)

        # Create a function to save the PNG file after rendering is complete
        def saveImage():
            # Specify the file path where you want to save the PNG file
            fileName = os.path.join(filePath, f"{currentTime}.png")

            # Save the PNG file
            self.wv.grab().save(fileName)

            # Reset the size of the self.wv widget to its original size
            self.wv.resize(originalSize)
            return

        # Set a QTimer to call the saveImage function after a short delay (e.g., 100 milliseconds)
        QTimer.singleShot(100, saveImage)
        return


    def kanjigridAuto(self):
        #self.did = mw.col.conf['curDeck']

        #dids = [self.did]
        #for name, id in mw.col.decks.children(self.did):
            #dids.append(id)
        #cids = mw.col.db.list("select id from cards where did in %s or odid in %s" % (ids2str(dids),ids2str(dids)))
        cids = mw.col.db.list("select id from cards")
        
        units = dict()
        notes = dict()
        timeNow = time.time()
        for id,i in enumerate(cids):
            card = mw.col.getCard(i)
            if card.nid not in notes.keys():
                keys = card.note().keys()
                unitKey = None
                matches = None
                if _literal:
                    matches = operator.eq
                else:
                    matches = operator.contains
                for keyword in _pattern:
                    for s,key in ((self.lower(key),key) for key in keys):
                        if matches(self.lower(s), keyword):
                            unitKey = card.note()[key]
                            break
                notes[card.nid] = unitKey
            else:
                unitKey = notes[card.nid]
            if unitKey != None:
                for ch in unitKey:
                    addUnitData(units, ch, i, card, timeNow)
        return units,timeNow
        
    def lower(self, inputString):
        global _caseSensitive
        return inputString if _caseSensitive else inputString.lower()
        
    def makegrid(self):
        global _group
        (units, timeNow) = self.kanjigridAuto()
        howManySorts = len(_chosenSortingMethods)
        for i in range(howManySorts):
            _group = _sortingMethods.index(_chosenSortingMethods[i]) #Only sets group equal to the index of the chosen sorting methods
            if units is not None:
                self.displaygrid(units, timeNow, _group)

    def setup(self):
        global _pattern, _literal, _filePathBase
        global _interval, _kanjiPerRowPNG, _wide
        global _group, _onlySeenKanji, _tooltips
        if _filePathBase == "":
            showInfo("Save folder not specified. Click Tools -> Kanji Grid Settings")
        else:
            swin = QDialog(mw)
            for i in range(len(_pattern)):
                _pattern[i] = self.lower(_pattern[i])
            mw.progress.start(immediate=True)   
            self.makegrid()
        
        mw.progress.finish()

if __name__ != "__main__":
    # Save a reference to the toolkit onto the mw, preventing garbage collection of PyQT objects
    if mw: mw.kanjigridAuto = KanjiGridAuto(mw)
else:
    print("This is a plugin for the Anki Spaced Repition learning system and cannot be run directly.")
    print("Please download Anki2 from <http://ankisrs.net/>")

# vim:expandtab:

