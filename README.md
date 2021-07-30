# CordaExample

CordaExample is a Corda Dapp project with the Springboot application. The example illustrate how to create DLT records to store the hash for loans such that the lender is able to read these hash to verify the loan records provided from the borrower.

## Installation

To build and deploy the Nodes
```bash
cd cordaexample
./gradlew deployNodes
./build/nodes/runnodes
```

To build and start the springboot application
```bash
cd cordaexample
./gradlew bootRun
```


## Usage

```python
import foobar

# returns 'words'
foobar.pluralize('word')

# returns 'geese'
foobar.pluralize('goose')

# returns 'phenomenon'
foobar.singularize('phenomena')
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
