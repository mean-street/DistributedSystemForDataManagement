# AWS

All the commands are to be executed on the user's machine, not on AWS.

## Install

```bash
# Can be launched in a virtualenv
# virtualenv venv
# source venv/bin/activate
# With Python 3
pip install awscli
pip install boto3
```

## Configure

cf. http://docs.aws.amazon.com/fr_fr/cli/latest/userguide/tutorial-ec2-ubuntu.html

```bash
# Register AWS credentials
aws configure
# AWS Access Key ID: See in the AWS Educate Lab page
# AWS Secret Access Key: See in the AWS Educate Lab page
# Default region name [None]: eu-west-3
# Default output format [None]: json

# Test API request
aws ec2 describe-regions --output table

# Configure
python configure.py
```

## Manage the machines

### Launch all instances

```bash
python launch_instances.py
```

### Terminate all instances

```bash
python terminate_instances.py
```

### List all instances

```bash
python get_instances.py
```

Or, in a Python script:

```python
from get_instances import get_instances
running_instances = get_instances()
terminated_instances = get_instances('terminated')
```