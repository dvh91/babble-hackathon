/**
 * Created by itayk on 02/01/17.
 */
"use strict";
    const demoSource = `<time=0.22>Then<time=0.48> <time=0.48>maybe<time=0.84> <time=0.84>you<time=1.14> <time=1.14>need<time=1.38> <time=1.38>a<time=1.44> <time=1.44>new<time=1.67> <time=1.67>lesson.<time=2.15> <time=2.5>Repeat<time=2.81> <time=2.81>after<time=3.15> <time=3.15>me.<time=3.45> <time=4.79>Hakuna<time=5.27> <time=5.38>Matata.<time=5.93>

<time=6.21>What?<time=6.68>

<time=6.96>Ha-ku-na<time=8.07> <time=8.3>Ma-ta-ta.<time=9.47> <time=9.97>It<time=10.1> <time=10.1>means<time=10.36> <time=10.36>"No<time=10.58> <time=10.58>worries."<time=11.12>


<time=11.84>akuna<time=12.36> <time=12.36>Matata!<time=13.05>
<time=14.46>What<time=14.79> <time=15.03>a<time=15.06> <time=15.16>wonderful<time=15.49> <time=15.67>phrase<time=16.34>
<time=17.8>Hakuna<time=18.2> <time=18.25>Matata!<time=18.86>
<time=19.92>Ain't<time=20.13> <time=20.23>no<time=20.42> <time=20.57>passing<time=21.12> <time=21.12>craze<time=21.32>
<time=21.32>It<time=21.38> <time=21.45>means<time=22.48> <time=23.95>no<time=24.27> <time=24.27>worries<time=25.35>
<time=25.69>For<time=26.04> <time=26.04>the<time=26.23> <time=26.29>rest<time=26.7> <time=26.7>of<time=26.81> <time=26.81>your<time=27.1> <time=27.1>days<time=28.91>

<time=29.3>It's<time=29.65> <time=29.65>our<time=29.99> <time=29.99>problem-free<time=32.15>
<time=33.09>Philosophy<time=33.92>
<time=34.81>Hakuna<time=35.12> <time=35.57>Matata!<time=36.12>

<time=37.85>Hakuna<time=38.09> <time=38.09>matata?<time=38.73>
<time=39.2>Yeah,<time=39.43> <time=39.52>it's<time=39.73> <time=39.73>our<time=39.99> <time=40.05>motto.<time=40.41>
<time=40.69>What's<time=40.96> <time=40.96>a<time=41.0> <time=41.0>motto?<time=41.41>
<time=41.85>Nothing!<time=42.22> <time=42.22>What's<time=42.41> <time=42.41>a<time=42.45> <time=42.45>motto<time=42.73> <time=42.73>with<time=42.86> <time=42.86>you?<time=43.17> <time=43.29>Ahh<time=43.46> <time=44.35>ha<time=44.55> <time=44.59>ha<time=44.85> <time=44.97>ha...<time=45.15>
<time=45.41>You<time=45.54> <time=45.54>know,<time=45.72> <time=45.82>kid--<time=46.0> <time=46.4>These<time=46.67> <time=46.67>two<time=46.89> <time=46.94>words<time=47.26> <time=47.26>will<time=47.46> <time=47.46>solve<time=47.87> <time=48.04>all<time=48.46> <time=48.46>your<time=48.72> <time=48.85>problems.<time=49.31>
<time=49.31>That's<time=49.63> <time=49.63>right!<time=49.88> <time=49.94>Take<time=50.19> <time=50.22>Pumbaa<time=50.6> <time=50.6>for<time=50.82> <time=50.82>example.<time=51.43>


<time=51.71>Why,<time=52.12> <time=52.74>when<time=52.99> <time=53.05>he<time=53.32> <time=53.36>was<time=53.54> <time=53.58>a<time=53.7> <time=53.7>young<time=54.1> <time=54.1>warthog...<time=54.59>
<time=55.07>When<time=55.47> <time=55.72>I<time=55.95> <time=56.39>was<time=56.59> <time=56.68>a<time=56.9> <time=56.97>young<time=57.13> <time=57.43>wart<time=57.72> <time=59.16>hoooog!<time=59.28>
<time=59.53>Very<time=59.79> <time=59.89>nice.<time=60.02>

<time=60.37>Thanks!<time=60.84>

 <time=61.55>He<time=61.73> <time=61.77>found<time=62.12> <time=62.12>his<time=62.28> <time=62.28>aroma<time=62.7> <time=62.7>lacked<time=62.97> <time=62.97>a<time=63.01> <time=63.01>certain<time=63.59> <time=63.59>appeal<time=64.04>
<time=64.04>He<time=64.15> <time=64.15>could<time=64.37> <time=64.37>clear<time=64.66> <time=64.66>the<time=64.75> <time=64.75>savannah<time=65.28> <time=65.4>after<time=65.78> <time=65.94>every<time=66.41> <time=66.41>meal<time=66.75>
	<time=66.81>I'm<time=66.92> <time=66.97>a<time=67.03> <time=67.03>sensitive<time=67.67> <time=67.67>soul,<time=68.63> <time=68.89>though<time=69.05> <time=69.05>I<time=69.18> <time=69.18>seem<time=69.61> <time=69.77>thick-skinned<time=71.39>
<time=71.82>And<time=71.94> <time=72.93>it<time=73.08> <time=73.23>hurt<time=73.43> <time=74.09>that<time=74.18> <time=74.18>my<time=74.33> <time=74.42>friends<time=74.67> <time=74.82>never<time=75.03> <time=75.03>stood<time=75.46> <time=75.67>downwind<time=77.07>
<time=77.07>And<time=77.43> <time=78.24>oh,<time=78.27> <time=78.94>the<time=79.05> <time=80.1>shame<time=80.2>
<time=80.2>He<time=80.3> <time=80.67>was<time=80.76> <time=80.93>ashamed!<time=81.23>
<time=81.23>Thoughta<time=81.4> <time=81.5>changin'<time=81.87> <time=81.87>my<time=82.09> <time=82.09>name<time=82.64>

<time=83.62>And<time=84.16> <time=84.16>I<time=84.26> <time=84.26>got<time=84.45> <time=84.53>downhearted<time=84.92>

<time=84.92>Ev'rytime<time=85.2> <time=85.2>that<time=85.57> <time=87.4>I...<time=87.73>
<time=87.73>Pumbaa!<time=88.1> <time=88.31>Not<time=88.49> <time=88.49>in<time=88.55> <time=88.55>front<time=88.73> <time=88.73>of<time=88.79> <time=88.79>the<time=88.9> <time=88.9>kids!<time=89.43>
<time=89.47>Oh...<time=89.65> <time=89.83>sorry.<time=90.27>



<time=91.05>Hakuna<time=91.34> <time=91.59>Matata!<time=91.94>
<time=93.67>What<time=93.85> <time=93.85>a<time=93.91> <time=93.96>wonderful<time=94.42> <time=94.88>phrase<time=95.34>
<time=96.72>Hakuna<time=97.24> <time=97.28>Matata!<time=97.63>
<time=98.95>Ain't<time=99.28> <time=99.28>no<time=99.47> <time=99.67>passing<time=100.21> <time=100.38>craze<time=101.45>
<time=102.34>It<time=102.42> <time=102.42>means<time=102.85> <time=103.24>no<time=103.36> <time=103.57>worries<time=103.96>
<time=103.96>For<time=104.05> <time=104.57>the<time=104.8> <time=105.17>rest<time=105.57> <time=105.57>of<time=105.63> <time=105.63>your<time=105.72> <time=105.92>days<time=106.32>
<time=106.32>Yeah,<time=106.71> <time=106.91>sing<time=107.1> <time=107.1>it,<time=107.21> <time=107.35>kid!<time=107.59>
<time=108.15>It's<time=108.51> <time=108.51>our<time=108.83> <time=108.85>problem-free<time=109.92> <time=109.92>..........<time=111.79>
 <time=111.93>.....................<time=113.63> <time=113.63>philosophy...<time=113.92>
<time=114.08>Hakuna<time=114.28> <time=114.41>Matata!<time=114.93>

<time=117.61>Welcome...<time=118.1> <time=118.32>to<time=118.47> <time=118.47>our<time=118.71> <time=118.88>humble<time=119.33> <time=119.33>home.<time=119.73>
<time=119.9>You<time=120.09> <time=120.09>live<time=120.22> <time=120.6>here?<time=120.83>
<time=121.07>We<time=121.2> <time=121.2>live<time=121.37> <time=121.45>wherever<time=121.74> <time=121.74>we<time=121.86> <time=121.86>want.<time=122.18>
	<time=122.6>Yep.<time=122.91> <time=123.09>Home<time=123.3> <time=123.3>is<time=123.44> <time=123.44>where<time=123.55> <time=123.55>your<time=123.75> <time=123.75>rump<time=123.98> <time=124.02>rests.<time=124.49> <time=124.49>Heh!<time=124.57>
<time=125.07>It's<time=125.26> <time=125.57>beautiful.<time=125.93>
 <time=127.86>I'm<time=128.1> <time=128.1>starved.<time=128.84>
 <time=130.09>Eeeahhah.<time=130.47> <time=132.62>We're<time=132.76> <time=132.76>fresh<time=133.16> <time=133.16>out<time=133.33> <time=133.33>of<time=133.4> <time=133.4>zebra.<time=133.82>
<time=134.14>Any<time=134.26> <time=134.35>antelope?<time=134.84>
<time=135.23>Na<time=135.41> <time=135.53>ah.<time=135.56>
 <time=135.91>Hippo?<time=136.32>
<time=136.72>Nope.<time=137.09> <time=137.59>Listen,<time=137.88> <time=137.97>kid;<time=138.24> <time=138.44>if<time=138.56> <time=138.56>you<time=138.68> <time=138.68>live<time=138.89> <time=138.89>with<time=139.01> <time=139.01>us,<time=139.07> <time=139.36>you<time=139.44> <time=139.44>have<time=139.59> <time=139.65>to<time=139.76> <time=139.76>eat<time=139.96> <time=139.96>like<time=140.19> <time=140.23>us.<time=140.29> <time=140.77>Hey,<time=141.12> <time=141.12>this<time=141.34> <time=141.34>looks<time=141.52> <time=141.52>like<time=141.67> <time=141.67>a<time=141.71> <time=141.71>good<time=141.91> <time=141.91>spot<time=142.19> <time=142.23>to<time=142.31> <time=142.31>rustle<time=142.64> <time=142.64>up<time=142.77> <time=142.77>some<time=142.92> <time=142.95>grub.<time=143.31>


<time=144.59>Eeew.<time=144.68> <time=145.01>What's<time=145.39> <time=145.82>that?<time=145.93>
<time=145.99>A<time=146.15> <time=146.2>grub.<time=146.53> <time=146.78>What's<time=147.21> <time=147.21>it<time=147.29> <time=147.29>look<time=147.43> <time=147.51>like?<time=147.69>
<time=147.97>Eeew.<time=148.26> <time=148.86>Gross.<time=149.18>
 <time=149.23>Mmmm.<time=149.29> <time=151.35>Tastes<time=151.58> <time=151.58>like<time=151.77> <time=151.79>chicken.<time=152.18>

 <time=153.52>Slimy,<time=154.27> <time=154.27>yet<time=154.49> <time=154.59>satisfying.<time=155.62>
<time=156.35>These<time=156.6> <time=156.6>are<time=156.78> <time=156.85>rare<time=157.22> <time=157.22>delicacies.<time=157.84> <time=158.1>Mmmm.<time=158.19>  <time=159.76>Piquant,<time=160.12> <time=161.01>with<time=161.12> <time=161.12>a<time=161.18> <time=161.18>very<time=161.54> <time=161.74>pleasant<time=162.03> <time=162.1>crunch.<time=162.59>
<time=163.23>You'll<time=163.41> <time=163.41>learn<time=163.64> <time=163.81>to<time=163.91> <time=164.09>love<time=164.33> <time=164.33>'em.<time=164.54>
<time=164.6>I'm<time=164.78> <time=164.78>telling<time=165.08> <time=165.08>you,<time=165.19> <time=165.19>kid,<time=165.46> <time=165.93>this<time=166.08> <time=166.11>is<time=166.21> <time=166.21>the<time=166.31> <time=166.31>great<time=166.6> <time=166.6>life.<time=166.85> <time=167.31>No<time=167.51> <time=167.51>rules.<time=167.91> <time=168.0>No<time=168.13> <time=168.18>responsibilities.<time=169.19> <time=169.65>Oooh!<time=169.69> <time=170.05>The<time=170.13> <time=170.13>little<time=170.35> <time=170.38>cream-filled<time=170.99> <time=171.08>kind.<time=171.31> <time=172.34>And<time=172.55> <time=172.61>best<time=172.92> <time=172.92>of<time=173.07> <time=173.07>all,<time=173.43> <time=173.88>no<time=174.17> <time=174.17>worries.<time=174.79>

	<time=175.02>Well,<time=175.3> <time=175.35>kid?<time=175.59>
<time=176.0>Oh<time=176.03> <time=177.32>well--<time=177.6> <time=178.09>Hakuna<time=178.65> <time=182.27>Matata.<time=183.23>

 <time=184.7>Slimy,<time=185.08> <time=186.03>yet<time=186.19> <time=186.27>satisfying.<time=187.02>
<time=187.58>hat's<time=188.07> <time=206.27>it!<time=206.71>

<time=206.71>Hakuna<time=207.05> <time=207.05>matata,<time=207.41> <time=207.48>hakuna<time=207.83> <time=208.96>matata,<time=209.21> <time=209.51>hakuna<time=210.94> <time=212.67>matata.<time=213.06>
<time=213.06>It<time=213.12> <time=213.31>means<time=214.43> <time=215.02>no<time=215.08> <time=216.04>worries<time=216.4>
<time=217.43>For<time=217.53> <time=218.14>the<time=218.28> <time=228.62>rest<time=228.87> <time=229.21>of<time=229.43> <time=230.21>your<time=230.35> <time=231.55>days.<time=231.66>
<time=232.3>It's<time=232.47> <time=232.87>our<time=233.06> <time=236.04>problem-free<time=237.53>
<time=237.53>Philosophy<time=238.26>
<time=238.99>Hakuna<time=239.77> <time=246.03>Matata<time=246.23>`;
const demoDest = `<time=0.03>Faux,<time=0.15> <time=0.24>quand<time=0.43> <time=0.43>le<time=0.49> <time=0.49>monde<time=0.76> <time=0.82>entier<time=1.1> <time=1.1>te<time=1.51> <time=1.59>persécute,<time=2.56> <time=2.8>tu<time=2.95> <time=2.95>as<time=3.03> <time=3.22>le<time=3.29> <time=3.29>droit<time=3.63> <time=3.63>de<time=3.73> <time=3.73>persécuter<time=4.84> <time=5.21>le<time=5.28> <time=5.28>monde.<time=5.49>
<time=5.49>Ce<time=5.62> <time=5.62>n’est<time=5.74> <time=5.74>pas<time=6.0> <time=6.0>ce<time=6.07> <time=6.07>qu’on<time=6.24> <time=6.24>m’a<time=6.5> <time=6.5>appris.<time=6.97>
<time=7.4>Il<time=7.47> <time=7.47>te<time=7.56> <time=7.67>faut<time=7.89> <time=7.89>peut-être<time=8.36> <time=8.36>une<time=8.53> <time=8.53>autre<time=8.84> <time=8.84>méthode.<time=9.15>  <time=9.15>Répète<time=9.37> <time=10.01>près<time=10.24> <time=10.24>moi.<time=10.52>  <time=11.61>“Hakuna<time=12.17> <time=12.35>Matata”<time=12.74>

<time=14.12>Quoi?<time=14.55> / [13.16-13.60]

<time=14.55>Hakuna<time=14.89> <time=15.15>Matata.<time=15.84>  <time=15.84>Ça<time=16.2> <time=16.72>veut<time=16.89> <time=16.89>dire,<time=17.0> <time=17.0>pas<time=17.28> <time=17.38>de<time=17.59> <time=20.89>soucis.<time=21.27>



<time=21.39>Hakuna<time=21.69> <time=21.69>Matata,<time=22.05> <time=22.44>quelle<time=22.57> <time=22.64>phrase<time=22.87> <time=22.97>magnifique<time=23.72>
<time=23.87>Hakuna<time=24.66> <time=24.66>Matata,<time=25.22> <time=26.08>quel<time=26.28> <time=26.34>son<time=26.68> <time=26.97>fantastique.<time=27.51>
<time=28.86>Ces<time=28.98> <time=28.98>mots<time=29.54> <time=29.84>signifient<time=30.55> <time=30.55>que<time=30.93> <time=31.94>tu<time=32.2> <time=32.2>vivras<time=32.69> <time=32.69>ta<time=32.94> <time=32.94>vie,<time=33.13> <time=34.99>sans<time=35.17> <time=35.59>aucun<time=35.9> <time=36.28>souci,<time=36.86> <time=40.16>philosophie...Hakuna<time=40.96> <time=41.07>Matata<time=41.55>
<time=43.15>Hakuna<time=43.71> <time=43.76>Matata?<time=44.23>
<time=44.23>Mais<time=44.32> <time=44.85>oui,<time=45.1> <time=45.1>c’est<time=45.29> <time=45.29>notre<time=45.42> <time=45.42>vieux<time=45.6> <time=45.6>crédo.<time=46.01>
<time=46.01>C’est<time=46.3> <time=46.34>quoi,<time=46.45> <time=46.45>un<time=46.64> <time=46.64>crédo?<time=47.01>
<time=47.17>C’est<time=47.4> <time=47.4>Pumbaa<time=47.68> <time=47.82>le<time=47.97> <time=47.97>vieux<time=48.13> <time=48.13>gras-dos.<time=48.72>
<time=50.62>C’est<time=50.81> <time=50.81>fastache!<time=51.32>  <time=51.78>Ces<time=52.06> <time=52.06>deux<time=52.36> <time=52.36>mots<time=52.48> <time=52.48>règleront<time=53.27> <time=53.27>tous<time=53.47> <time=53.62>tes<time=53.9> <time=53.9>problèmes.<time=54.4>
<time=54.42>C’est<time=54.57> <time=54.57>vrai,<time=54.75> <time=55.21>tiens<time=55.57> <time=55.76>Pumbaa<time=56.07> <time=56.26>par<time=56.55> <time=57.8>example,<time=58.47> <time=58.47>en<time=58.59> <time=58.68>bien,<time=58.77> <time=58.87>ce<time=58.9> <time=58.9>très<time=59.02> <time=59.02>jeune<time=59.23> <time=59.39>phacochère.<time=60.28>
<time=63.9>Mélo-bien<time=64.6>
<time=65.08>Merci.<time=65.47>
<time=66.04>Un<time=66.25> <time=66.28>jour<time=66.61> <time=66.61>qu’elle<time=67.02> <time=67.02>aura<time=67.12> <time=67.29>compris<time=67.6> <time=67.6>que<time=67.72> <time=67.72>son<time=67.93> <time=67.93>odeur<time=68.11> <time=68.23>au<time=68.32> <time=68.54>lieu<time=68.65> <time=68.65>de<time=68.79> <time=68.79>sentir<time=69.07> <time=69.07>l’ail<time=69.27> <time=69.56>sur<time=69.63> <time=69.9>le<time=70.08> <time=70.08>bel<time=70.43> <time=70.43>écoeur.<time=70.7>
<time=70.7>Mais<time=70.8> <time=70.8>il<time=71.01> <time=71.01>y<time=71.12> <time=71.14>a<time=71.31> <time=71.37>d’autres<time=71.77> <time=71.77>cochons,<time=72.13> <time=73.1>un<time=73.27> <time=73.27>poète<time=73.57> <time=73.57>qui<time=73.93> <time=73.93>a<time=73.96> <time=73.96>sommeil,<time=74.33> <time=76.71>quel<time=77.03> <time=77.03>martyr<time=77.42> <time=78.97>quand<time=79.15> <time=79.15>personne<time=79.36> <time=80.12>ne<time=80.26> <time=85.67>peux<time=85.78> <time=87.85>plus<time=88.18> <time=88.18>vous<time=88.34> <time=91.05>sentir,<time=91.27> <time=91.27>disgrâce,<time=91.58> <time=91.58>infâme...<time=91.84>
<time=91.9>Quelle<time=92.03> <time=92.03>honte<time=92.2> <time=92.2>de<time=92.26> <time=92.26>mon<time=92.42> <time=92.42>âme.<time=92.51>
<time=92.51>Quelle<time=92.82> <time=92.82>honte<time=93.01> <time=93.2>de<time=93.26> <time=93.26>mon<time=93.5> <time=93.5>âme.<time=93.62>  <time=94.46>Ça<time=94.52> <time=94.52>déclenche<time=95.23> <time=95.23>une<time=95.34> <time=95.4>trompette..chaque<time=96.18> <time=96.86>fois<time=96.99> <time=97.77>que<time=98.03> <time=98.08>je..<time=98.56>
<time=100.01>Non<time=100.3> <time=100.6>Pumbaa...pas<time=101.07> <time=101.19>devant<time=101.6> <time=101.9>les<time=102.21> <time=102.4>enfants.<time=103.05>
<time=110.04>Hakuna<time=110.39> <time=110.8>Matata,<time=111.42> <time=111.45>quelle<time=111.73> <time=111.99>phrase<time=112.56> <time=114.2>magnifique.<time=114.64>  <time=116.18>Hakuna<time=116.43> <time=116.86>Matata,<time=117.32> <time=119.85>quel<time=120.1> <time=120.65>son<time=120.83> <time=120.92>magnifique.<time=121.91>
<time=123.49>Ce<time=123.53> <time=123.75>mot<time=123.89> <time=124.61>signifie<time=125.21> <time=125.28>que<time=125.45> <time=125.61>tu<time=125.77> <time=125.85>vivras<time=126.38> <time=126.77>ta<time=126.89> <time=127.26>vie.<time=127.39>
<time=127.92>Oui<time=128.21> <time=130.14>chante<time=130.59> <time=130.59>petit.<time=130.87>
<time=130.96>Sans<time=131.18> <time=131.18>aucun<time=131.34> <time=131.46>souci,<time=131.73> <time=131.83>philosophie,<time=132.75> <time=133.52>Hakuna<time=134.32> <time=134.72>Matata.<time=135.08>
<time=135.08>Bienvenue<time=135.59> <time=136.4>dans<time=136.68> <time=137.56>notre<time=138.1> <time=139.26>humble<time=139.51> <time=139.65>chez<time=139.96> <time=140.46>nous.<time=140.6>
<time=140.65>Vous<time=140.84> <time=140.84>vivez<time=141.11> <time=141.79>ici?<time=141.97>
<time=142.45>Ça<time=142.62> <time=142.62>dépend<time=143.04> <time=143.04>des<time=143.1> <time=143.1>jours.<time=143.35>
<time=143.58>Oui,<time=143.84> <time=143.9>il<time=144.01> <time=144.07>faut<time=144.21> <time=144.21>laisser<time=144.46> <time=144.57>les<time=144.65> <time=146.51>fesses<time=146.78> <time=146.78>faire...<time=146.9>
<time=146.9>C’est<time=147.29> <time=147.47>merveilleux!<time=147.7>
<time=147.7>J’ai<time=147.92> <time=147.92>un<time=148.09> <time=148.09>petit<time=148.36> <time=148.47>creux..<time=148.57>
<time=148.57>Eh<time=148.6> <time=148.6>moi<time=148.74> <time=148.74>alors,<time=149.11> <time=149.81>je<time=149.92> <time=149.92>pourrais<time=150.13> <time=150.31>avaler<time=151.03> <time=152.39>un<time=152.91> <time=154.7>éléphant.<time=155.19>
<time=155.19>Eh,<time=155.22> <time=155.32>on<time=155.72> <time=155.77>a<time=155.82> <time=155.87>épuisé<time=156.23> <time=156.23>le<time=156.31> <time=157.26>stock<time=157.64> <time=157.72>d’éléphants.<time=158.46>
<time=158.46>Et<time=158.7> <time=161.75>il<time=161.97> <time=162.07>y<time=162.26> <time=162.36>a<time=162.41> <time=162.41>des<time=162.65> <time=162.79>antilopes?<time=163.46>
<time=164.02>Non<time=164.23>
<time=164.23>Et<time=164.35> <time=164.35>d’un<time=164.48> <time=164.48>hippo?<time=164.71>
<time=164.71>Non,<time=164.82> <time=164.85>écoute<time=165.02> <time=165.02>petit,<time=165.47> <time=165.69>si<time=165.92> <time=166.01>tu<time=166.11>  <time=166.11>vis<time=166.37> <time=166.57>avec<time=166.84> <time=166.88>nous,<time=167.03> <time=167.03>il<time=167.18> <time=167.18>va<time=167.29> <time=167.29>falloir<time=167.47> <time=167.47>d’adapter.<time=167.73>  <time=167.73>Eh<time=167.82> <time=167.82>le<time=167.9> <time=167.9>bois<time=168.38> <time=168.56>n’est<time=168.76> <time=168.76>pas<time=168.95> <time=169.31>mal<time=169.61> <time=169.79>pour<time=169.88> <time=170.65>une<time=170.98> <time=171.08>bonne<time=171.24> <time=172.61>pâté<time=172.85> <time=172.89>de<time=173.11> <time=173.11>larve.<time=173.33>
<time=174.13>Oh!<time=174.18>  <time=174.38>Qu’est-ce<time=174.68> <time=174.68>que<time=174.87> <time=175.14>c’est?<time=175.35>
<time=175.35>Une<time=175.44> <time=175.44>larve,<time=175.64> <time=175.72>bien<time=175.86> <time=177.39>grasse.<time=177.75>
<time=178.11>Ah,<time=178.19> <time=178.19>quelle<time=178.64> <time=181.62>horreur!<time=181.79>
<time=181.97>Mmm<time=182.15> <time=182.25>délicieux,<time=182.97> <time=183.13>on<time=183.34> <time=183.78>dirait<time=184.08> <time=184.59>du<time=184.8> <time=184.8>veau.<time=184.97>
 <time=184.97>Un<time=185.11> <time=185.11>peu<time=185.35> <time=185.35>gluant,<time=185.54> <time=185.64>mais<time=185.84> <time=185.84>appetissant.<time=186.78>

<time=187.34>Ils<time=187.66> <time=187.66>sont<time=187.95> <time=189.02>comme<time=189.18> <time=191.45>je<time=191.51> <time=191.67>les<time=192.1> <time=204.75>aime,<time=204.88> <time=205.4>bien<time=205.73> <time=205.73>fraîches,<time=205.91> <time=205.91>bien<time=206.12> <time=206.9>craquantes.<time=207.47>   <time=209.02>12<time=226.22>
<time=226.22>Tu<time=226.37> <time=226.49>finiras<time=226.76> <time=227.02>par<time=227.26> <time=227.32>t’habituer.<time=228.13>
<time=229.26>Je<time=229.34> <time=229.34>te<time=229.53> <time=229.76>dis<time=229.85> <time=229.87>petit,<time=230.0> <time=230.0>c’est<time=230.18> <time=230.18>ça<time=230.24> <time=230.41>la<time=230.5> <time=230.58>vraie<time=230.68> <time=230.68>vie.<time=230.74>  <time=230.74>Pas<time=230.88> <time=230.88>de<time=230.98> <time=230.98>lois,<time=231.24> <time=231.24>pas<time=231.34> <time=231.34>de<time=231.41> <time=231.41>responsabilités.<time=231.92>  <time=233.65>Oh!<time=233.9>  <time=233.9>Une<time=235.21> <time=235.21>petite<time=235.36> <time=235.36>punaise<time=235.72> <time=235.85>crémeuse.<time=236.51>  <time=236.59>Et<time=236.65> <time=236.65>le<time=236.72> <time=236.72>mieux<time=236.91> <time=237.06>de<time=237.18> <time=237.31>tout,<time=237.43> <time=237.43>pas<time=237.8> <time=237.8>de<time=237.86> <time=238.07>soucis.<time=238.22>  <time=238.49>Qu’est-ce<time=238.68> <time=239.63>que<time=239.72> <time=240.14>t’en<time=240.31> <time=240.31>dis?<time=240.49>
<time=241.09>Eh<time=241.12> <time=241.46>bien.<time=241.67>  <time=241.67>Hakuna<time=241.85> <time=241.85>Matata.<time=242.12>  <time=242.12>Un<time=242.18> <time=242.34>peu<time=242.45> <time=242.71>gluant<time=243.0> <time=243.0>mais<time=243.17> <time=243.23>appétissant<time=243.66>
<time=244.43>Et<time=244.53> <time=245.41>voilà.<time=245.82>
`;

let RecomendationEntry = {
     sourceStartTime: 0,
     sourceEndTime: 0,
     sourceLanguageText: "",
     translatedWord: "",
     targetStartTime: 0,
     targetEndTime: 0,
     targetLanguageText: ""
};

let TranslatedSpeechmaticsEntry = {
      startTime:  0,
      endTime:  0,
      word:  "",
      origin: {}

};

let SpeechmaticsEntry = {
      startTime:  0,
      endTime:  0,
      word:  ""
};


let EntryMapOptions = {
      entry: SpeechmaticsEntry,
      mapOptions: [TranslatedSpeechmaticsEntry]
};

class RecommendationEngine {
    generate( sourceContent , destinationContent ) {
        let _this = this;
        this.translatedOptionsBySourceWord = [];
        let sourceSpeechmaticsEntries = this.parseSpeechmaticsFile( sourceContent );
        let targetSpeechmaticsEntries = this.parseSpeechmaticsFile( destinationContent );
        this.loadTranslate( targetSpeechmaticsEntries,function(data){
            console.log(data)
            for (let index in sourceSpeechmaticsEntries){
                let entry = sourceSpeechmaticsEntries[index];
                let nearByEntries = _this.getNearByEntries(entry,data,1);
                let option = {entry:entry,mapOptions:nearByEntries};
                _this.translatedOptionsBySourceWord.push(option);
            }

            let recomendation = _this.getRecomendation()
            console.log(recomendation);
        });

    }

    getNearByEntries(entry,entries,offset) {


        let result =  entries.filter((obj)=>{
            return (obj.startTime > (entry.startTime-offset) && obj.startTime < (parseFloat(entry.endTime)+offset)) &&
                (obj.endTime > (entry.startTime-offset) && obj.endTime < (parseFloat(entry.endTime)+offset))
        })

        return result;


    }

    getRecomendation() {

        var recomendations = [];
        let matchesEntries = [];
        for ( var index in this.translatedOptionsBySourceWord ) {
            let entryOption = this.translatedOptionsBySourceWord[index];
               matchesEntries = entryOption.mapOptions.filter((entry) => {
                 let cleanWord =entryOption.entry.word.match(/[a-zA-Z]*/)[0];
                 let cleanWord2 =entry.word.match(/[a-zA-Z]*/)[0];
                 return cleanWord.localeCompare(cleanWord2);// entryOption.entry.word.localeCompare(entry.word) == 0
             });
            let match = matchesEntries[matchesEntries.length-1];
            if ( match ) {
                let recomendation = {
                    sourceEndTime: entryOption.entry.endTime ,
                    sourceLanguageText: entryOption.entry.word ,
                    translatedWord: match.word ,
                    targetStartTime: match.startTime ,
                    targetEndTime: match.endTime ,
                    targetLanguageText: match.word

                };

                recomendations.push( recomendation )
            }


        }




        return recomendations;
    }

    loadTranslate( sourceSpeechmaticsEntries , completion ) {
        let stringsToTranslate = sourceSpeechmaticsEntries.map( ( obj ) => {
            return obj.word;
        } );
        let urlArray = [];

        let requestNumber = stringsToTranslate.count / 128;
        if ( stringsToTranslate.count % 128 != 0 ) {
            requestNumber++;
        }

        let wordsCountPerRequest = 128;
        let requestIndex = 0;
        for ( var i in stringsToTranslate ) {
            let word = stringsToTranslate[i];
            if ( wordsCountPerRequest == 0 ) {
                wordsCountPerRequest = 128
                requestIndex = requestIndex + 1
            }
            if ( wordsCountPerRequest == 128 ) {
                urlArray.push( "https://translation.googleapis.com/language/translate/v2?key=AIzaSyDlH3tp1UBwQoPLgZy7KIhJxg5tazAAgQE&source=fr&target=en" )
            }
            let param = "&q=" + word;
            // if let p = param {
            urlArray[requestIndex] = urlArray[requestIndex] + param;
            //  }
            wordsCountPerRequest = wordsCountPerRequest - 1

        }
        var finalResult = [];
        var currentResultIndex = 0;
        var urlIndex = 0;
        var callback = function ( data ) {
            var needToAdd = currentResultIndex * 128;
            for ( var index in data.data.translations ) {
                let currentWord = data.data.translations[index];
                try {
                    let result = {
                        startTime: sourceSpeechmaticsEntries[parseInt( index) + needToAdd ].startTime ,
                        endTime: sourceSpeechmaticsEntries[parseInt( index ) + needToAdd ].endTime ,
                        word: currentWord.translatedText
                    }
                    finalResult.push( result );
                }catch(e){
                    debugger;
                }
            }
            currentResultIndex++;
            let url = urlArray[++urlIndex];
            if ( url ) {
                $.get( url , null , callback )
            }
            else {
                completion(finalResult);
            }
        };
        let url = urlArray[urlIndex];
        $.get( url , null , callback );
    }








    speechmaticsEntry(text) {
        let regex = /<time=[0-9]*\.[0-9]*>/ig;
        let nsString = text;
        let results = text.match( regex );
        if ( results && results.length == 2 ) {

            let word = text.match(/>(.*)</)[1];
            let startTimeString = results[0].match(/[0-9]*\.[0-9]*/)[0]
            let endTimeString = results[1].match(/[0-9]*\.[0-9]*/)[0]

            let startTime = startTimeString;
            let endTime = endTimeString;
            if ( startTime && endTime ) {
                let result ={
                    startTime:startTime,
                    endTime:endTime,
                    word:word
                };
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
    parseSpeechmaticsFile(content){
        let words = content.split(" ");
        let result = [];
        for (var i in words){
            if (words[i]){
                let entry = this.speechmaticsEntry(words[i]);
                if (entry){
                    result.push(entry);
                }
            }
        }
        return result;
    }
}

