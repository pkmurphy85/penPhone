% Next steps -- generalize process
% SVM has no online learning algos -- so we need to batch learn.
% Cannot update the SVM on the fly. 
% Questions -- user training data.
% Transfer functionality to Java
% online all the functioanlity. 
% use double tap as character separator for now. 

load('A_PM.mat');
load('B_PM.mat');
%Get the max dimension

maxPad = max(size(A,2),size(B,2));
zeroExt = zeros(25,maxPad-size(A,2));

%take FFT of observation for each spatial dimension
Ax = A(:,:,1);
Ax = [Ax,zeroExt];
Ay = A(:,:,2);
Ay = [Ay,zeroExt];
Az = A(:,:,3);
Az = [Az,zeroExt];

AxFFT = fft(Ax')';
AyFFT = fft(Ay')';
AzFFT = fft(Az')';
Bx = B(:,:,1);
By = B(:,:,2);
Bz = B(:,:,3);
BxFFT = fft(Bx')';
ByFFT = fft(By')';
BzFFT = fft(Bz')';
%create classifier for each spatial dimension
%create the labels
labels  = cell(1, 50);
labels(1:25) = {'A'};
labels(26:50) = {'B'};
%create indices for testing and training
indices = zeros(50,1);
indices(randsample(50,30)) = 1;
indices = logical(indices);
%get training sets
labels_train = labels(indices);
labels_test = labels(~indices);


xFFT = vertcat(AxFFT, BxFFT);
xFFT_train = xFFT(indices,:);
xFFT_test = xFFT(~indices,:);

yFFT = vertcat(AyFFT, ByFFT);
yFFT_train = yFFT(indices,:);
yFFT_test = yFFT(~indices,:);


zFFT = vertcat(AzFFT, BzFFT);
zFFT_train = zFFT(indices,:);
zFFT_test = zFFT(~indices,:);


xClass = fitcsvm(abs(xFFT_train),labels_train);
xhats = predict(xClass,abs(xFFT_test));
yClass = fitcsvm(abs(yFFT_train),labels_train);
yhats = predict(yClass,abs(yFFT_test));
zClass = fitcsvm(abs(zFFT_train),labels_train);
zhats = predict(zClass,abs(zFFT_test));
%Compare the actual labels to predictions
%We get 100% correct classification for this data
isequal(xhats,labels_test')
isequal(yhats,labels_test')
isequal(zhats,labels_test')
%% Visualize some FFT
subplot(2,3,1)
plot(A(1,:,1))
title('x');
ylabel('m/s^2');
xlabel('n-Time');
subplot(2,3,2)
plot(A(1,:,2))
title('y');
subplot(2,3,3)
plot(A(1,:,3))
title('z');
subplot(2,3,4)
plot(abs(fft(A(1,:,1))))
ylabel('|FFT|')
xlabel('n-Freq');
subplot(2,3,5)
plot(abs(fft(A(1,:,2))))
subplot(2,3,6)
plot(abs(fft(A(1,:,3))))

%% Classifier Strategy for additional characters,
% Test performance and speed for One Versus One and One Verrsus All 
% May need to use differenct strategy for character and string inputs.
% OVO is slow for testing, this would be very slow for string inputs. Maybe
% doo a OVA for character recognition from string input and then after we
% know we have a character use OVO to test what character it is. 
%% Data Processing Strategy for Arbitray Inputs -- Characters
%given the intervals
%grab the first interval. y(n+1)-y(n)
% put it into an arry
%grab the next interval and grow storage matrix if necessary
% if the current interval is larger than the last, pad the matrix
% if the current interval is smaller than width of matrix, pad the curret.
%% Data Processing Strategy for Arbitray Inputs -- Characters II
% explore under and oversampling strategies in DSP for arbitrary inputs.
%% Data Processing Strategy for Arbitray Inputs -- Strings
%consistently sample windows and check against all classifiers -- at some
%empirical threashold, call it a character.
%split signal per some threashold
%% Misc Notes
%put into multidimensional array
%matrix A(1) has the info for the first sample;
%A(2) has info for the second sample.
% http://blogs.mathworks.com/steve/2010/06/25/plotting-the-dtft-using-the-output-of-fft/
