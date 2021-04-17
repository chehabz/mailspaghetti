# Template Engine 

> The following is a curl command on how to generate a template and pass parameters. You must request the template param and the other tokens which are requested by the template.


**Creating an Email Templates**

Jump to [Email Repo](https://github.com/mailspaghetti/html-templates) to checkout how email templates are created.

```html
<html>
  <head>
    <style>h1{ color: red }</style>
  </head>
  <body>
    <h1>$title$</h1>
    <div>
        <span>Thank You $data.customer.name$ for your order find the summary below</span>
    </div>
    <div>
        
       <table>
          <tr>
            <td>#</td>
            <td>Description</td>
            <td>Quantity</td>
            <td>Price</td>
            <td>Total</td>
          </tr>
        $data.orders : {
         <tr>
            <td>$it.id$</td>
            <td>$it.description$</td>
            <td>$it.qty$</td>
            <td>$it.price$</td>
            <td>$it.total$</td>
         </tr>
        }$
      </table>
    </div>
    
    <div>
      <h2>Payment was done by credit card visa ...</h2>
    </div>
  </body>
</html>
```

**Json as template**

when you pass your json template you need to wrap it up in the **"data"** key

**Building and Running**

```bash
mvn clean package
```

```bash
mvn  -DSERVER_SERVLET_CONTEXT_PATH="/template-engine" -DACCESSTOKEN="" -DHOSTNAME="localhost" -DCHROMEPATH="/opt/chromedriver-74.0.3729.6"
```

**Checkout the results**

```bash
   curl -d 'data={"customer":{"name":"John Smith"},"orders":[{"id":1,"description":"Lorem ipsum","qty":1,"price":"1.5","total":"1.5"},{"id":2,"description":"Lorem ipsum","qty":1,"price":"1.5","total":"1.5"},{"id":3,"description":"Lorem ipsum","qty":1,"price":"1.5","total":"1.5"}]}' 'http://localhost:8086/template-engine/pdf/create?template=demo&title=Orders'
```

**Notes**
The project uses [headless-chrome](https://developers.google.com/web/updates/2017/04/headless-chrome) to generate the pdf.

**Run it in a container**

```bash
 docker build --rm -f "Dockerfile" -t templateEngine:tag .
 docker run -e SERVER_SERVLET_CONTEXT_PATH="/pdfmaker" -e ACCESSTOKEN="" -e HOSTNAME="localhost" -e CHROMEPATH="/opt/chromedriver-74.0.3729.6"  -p 8080:8080
 ```
