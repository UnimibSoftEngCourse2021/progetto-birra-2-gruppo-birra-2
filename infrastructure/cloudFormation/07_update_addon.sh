aws eks update-addon \
  --cluster-name brewday-prod-cluster \
  --addon-name vpc-cni \
  --service-account-role-arn arn:aws:iam::AKIAJPBEISKNEBOOCACA:role/myAmazonEKSCNIRole 