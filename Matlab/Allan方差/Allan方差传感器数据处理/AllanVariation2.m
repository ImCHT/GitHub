%   根据子集数据数确定主循环
%   data:数据数组   data_length：数据量   subset_data_num：子集数据数   subset_sum:子集数据和
%   i:分组数   j:均值数据索引  l:计算均值数组艾伦方差  average：均值数组   allan_variance:艾伦方差数组
%   AllanVariance:单个艾伦方差   subset_num:子集数
% clc;
clear;
%读取数据，初始化变量，将电压转换成加速度，加速度=电压/分辨率*9.8
%150714117分辨率=（1.336+0.045）V/g=1.381V/g
%1607072013分辨率=（1.163-0.040）V/g=1.123V/g    
data = xlsread('NI9239.xlsx','NI9239','B:B');%读取数据
data = data/1.381;  %V->g

data_length = length(data); %数据数量
allan_variance=zeros(floor(data_length/2),1);%艾伦方差结果数组
average=zeros(data_length,1);%均值数组

%求Allan方差
for i=1:floor(data_length/2) %子集数据数
    subset_data_num =  i;
    subset_num=floor(data_length/i);
    average=zeros(data_length,1);
    %分组求均值
    for j=1:subset_num  %均值数组索引
        subset_sum=0;
        for k=1:subset_data_num
            subset_sum=subset_sum + data((j-1)*subset_data_num+k);
        end
        average(j)=subset_sum/subset_data_num;
    end
    %计算各组Allan方差
    AllanVariance=0;
    for l=1:(subset_num-1)
        AllanVariance=AllanVariance+(average(l+1)-average(l))^2;
    end
    allan_variance(i,1)=AllanVariance/(2*(subset_num-1));
end
    %求Allan标准差
for i=1:floor(data_length/2)
    allan_variance(i,1)=sqrt(allan_variance(i,1));
end
%画双对数坐标图
figure(1)
X=linspace(0.1,floor(data_length/2)*0.1,floor(data_length/2));
plot(X,allan_variance);
%plot(allan_variance)
set(gca,'XScale','log'); 
set(gca,'YScale','log');
xlabel('平均时间(s)');
ylabel('Allan标准差(g)');
title('Allan方差分析结果');
