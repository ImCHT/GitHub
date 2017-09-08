%曲线拟合
% clc;
clear;
%读取数据
h1 = openfig('150714117.fig','reuse'); % open figure  
D1=get(gca,'Children'); %get the handle of the line object  
XData1=get(D1,'XData'); %get the x data  
YData1=get(D1,'YData'); %get the y data 
LogXdata1 = log10(XData1);
LogYdata1 = log10(YData1);
LogXdata2 = LogXdata1(13230:29460);
LogYdata2 = LogYdata1(13230:29460);
%图表说明
%title('\alpha_{0}^{2}+\beta_{0}^{2}=1')
axis([10^(-1) 10^5 10^(-7) 10^(-1)]);
% xlabel('平均时间(s)');
% ylabel('艾伦标准差(m/s^2)');
title('150714117Allan方差分析结果');
hold on;
%斜率为-1直线
x1 = linspace(XData1(1),XData1(end));
y1 = (10^(-4.676))*(x1.^(-1));
%y1 = log10(-1*10.^(x1)-4.788);
plot(x1,y1,'k--');
%斜率为-1/2直线
x1 = linspace(XData1(1),XData1(end));
y1 = (10^(-4.463))*(x1.^(-0.5));
%y1 = log10(-1*10.^(x1)-4.788);
plot(x1,y1,'k--');
%斜率为0直线
line([10^(-1),10^(5)],[10^(-5.011),10^(-5.011)],'color','k','linestyle','--');
%斜率为1/2直线
x1 = linspace(XData1(1),XData1(end));
y1 = (10^(-5.64))*(x1.^(0.5));
%y1 = log10(-1*10.^(x1)-4.788);
plot(x1,y1,'k--');
%斜率为1直线
x1 = linspace(XData1(1),XData1(end));
y1 = (10^(-7.141))*(x1);
%y1 = log10(-1*10.^(x1)-4.788);
plot(x1,y1,'k--');
%误差计算
Q = (10^(-4.676))*(sqrt(3).^(-1));
R = (10^(-4.463))*(1.^(-0.5));
B = 10^(-5.011)*0.6648;
K = (10^(-5.64))*(3.^(0.5));
T = (10^(-7.141))*(sqrt(2));
%误差显示
text(10^(0),10^(-2.5),['quantization noise=',num2str(Q),' g·s']);
text(10^(0),10^(-2.8),['velocity random walk=',num2str(R),' g·s^1^/^2']);
text(10^(0),10^(-3.1),['bias instability=',num2str(B),' g']);
text(10^(0),10^(-3.4),['acceleration random walk=',num2str(K),' g·s^-^1^/^2']);
text(10^(0),10^(-3.7),['acceleration trend=',num2str(T),' g·s^-^1']);
text(10^(3),10^(-3),['灵敏度=',num2str(1.381),'V/g']);



