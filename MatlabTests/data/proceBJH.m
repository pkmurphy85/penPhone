B_JH1 = BJH(y(1)+75:y(2),:);
B_JH2 = BJH(y(2):y(3),:);
B_JH2 = vertcat(B_JH2,zeros(2011-size(B_JH2,1),4));
B_JH3 = BJH(y(3):y(4),:);
B_JH3 = vertcat(B_JH3,zeros(2011-size(B_JH3,1),4));
B_JH4 = BJH(y(4):y(5),:);
B_JH4 = vertcat(B_JH4,zeros(2011-size(B_JH4,1),4));
B_JH5 = BJH(y(5):y(6),:);
B_JH5 = vertcat(B_JH5,zeros(2011-size(B_JH5,1),4));
B_JH6 = BJH(y(6):y(7),:);
B_JH6 = vertcat(B_JH6,zeros(2011-size(B_JH6,1),4));
B_JH7 = BJH(y(7):y(8),:);
B_JH7 = vertcat(B_JH7,zeros(2011-size(B_JH7,1),4));
B_JH8 = BJH(y(8):y(9),:);
B_JH8 = vertcat(B_JH8,zeros(2011-size(B_JH8,1),4));

xJHtest = [B_JH1(:,1),B_JH2(:,1),B_JH3(:,1),B_JH4(:,1),B_JH5(:,1),B_JH6(:,1),B_JH7(:,1),B_JH8(:,1)];
xJHtest=xJHtest';
xJHtestFFT = fft(xJHtest);
xJHtesthats = predict(xClass,abs(xJHtestFFT));