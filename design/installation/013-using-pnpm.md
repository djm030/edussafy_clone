# Using pnpm
pnpm add @primevue/forms
```

## Dynamic

This section demonstrates how to create a dynamic form using a custom Form component. It showcases an example where form fields are generated dynamically based on the provided configuration, allowing for flexible form structures. The components named Dynamic* shown in this example are not built-in, and only available for sampling purposes. First form uses a declarative approach whereas second form goes for a programmatic approach. We suggest running this sample in StackBlitz to view the comprehensive implementation.

```vue
<Fieldset legend="Form 1" pt:content:class="flex justify-center">
    <DynamicForm @submit="onFormSubmit('Form 1', $event)">
        <DynamicFormField groupId="userId_1" name="username">
            <DynamicFormLabel>Username</DynamicFormLabel>
            <DynamicFormControl defaultValue="PrimeVue" fluid :schema="userNameSchema" />
            <DynamicFormMessage />
        </DynamicFormField>
        <DynamicFormField groupId="passId_1" name="password">
            <DynamicFormLabel>Password</DynamicFormLabel>
            <DynamicFormControl as="Password" :feedback="false" toggleMask fluid :schema="passwordSchema" />
            <DynamicFormMessage errorType="minimum" />
            <DynamicFormMessage errorType="maximum" />
            <DynamicFormMessage errorType="uppercase" severity="warn" />
            <DynamicFormMessage errorType="lowercase" severity="warn" />
            <DynamicFormMessage errorType="number" severity="secondary" />
        </DynamicFormField>
        <DynamicFormSubmit />
    </DynamicForm>
</Fieldset>

<Fieldset legend="Form 2" pt:content:class="flex justify-center">
    <DynamicForm :fields @submit="onFormSubmit('Form 2', $event)" />
</Fieldset>
```

## Import

The form component is responsible for managing the form state and must encapsulate the form fields.

```vue
import { Form } from '@primevue/forms';
```

## Resolvers

Validations are implemented with the resolver property. A custom resolver is responsible for handling the validation and returning an errors object with key-value pairs where key is the form field name and value is an array of error object data. For productivity, we recommend using a schema validation library instead of building your own custom validation logic. The forms library provide built-in resolvers for popular options including Zod , Yup , Joi , Valibot , and Superstruct that can be imported from @primevue/forms/resolvers path.

```vue
<Form v-slot="$form" :initialValues :resolver="resolver" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <div class="flex flex-col gap-1">
        <InputText name="username" type="text" placeholder="Username" fluid />
        <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{ $form.username.error.message }}</Message>
    </div>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## States

The $form object tracks the state management of the fields. Each field is linked with the name property. View the FormFieldState type in the API documentation for details about each property.

```vue
<Form v-slot="$form" :initialValues :resolver @submit="onFormSubmit" class="grid lg:grid-cols-2 gap-4 w-full">
    <div class="flex flex-col justify-center items-center gap-4">
        <InputText name="username" type="text" placeholder="Username" class="w-full sm:w-56" />
        <Button type="submit" severity="secondary" label="Submit" class="w-full sm:w-56" />
    </div>
    <Fieldset legend="Form States" class="h-80 overflow-auto">
        <pre class="whitespace-pre-wrap">{{ $form }}</pre>
    </Fieldset>
</Form>
```

## Submit

The submit callback returns an object that encapsulates the form's validity, any existing errors, and its current state. This enables access to the form values, validation status, and any errors present at the time of submission. View the FormSubmitEvent in the API documentation for more information about the available event data.

```vue
<Form v-slot="$form" :initialValues :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-60">
    <div class="flex flex-col gap-1">
        <InputText name="username" type="text" placeholder="Username" fluid />
        <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{ $form.username.error.message }}</Message>
    </div>
    <div class="flex flex-col gap-1">
        <Password name="password" placeholder="Password" :feedback="false" toggleMask fluid />
        <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">
            <ul class="my-0 px-4 flex flex-col gap-1">
                <li v-for="(error, index) of $form.password.errors" :key="index">{{ error.message }}</li>
            </ul>
        </Message>
    </div>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## ValidateOn

Form component supports flexible validation triggers, allowing validation on value updates, blur events, form mount, or submission. These behaviors can be configured at form level or on specific fields via the validateOnValueUpdate , validateOnBlur , validateOnMount , and validateOnSubmit options of the formControl property. In this example, form disables validateOnValueUpdate and enables validateOnBlur at form level, and validates firstName on mount. The firstName field, overrides the form level setting locally.

```vue
<Form v-slot="$form" :initialValues :resolver :validateOnValueUpdate="false" :validateOnBlur="true" :validateOnMount="['firstName']" @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <div class="flex flex-col gap-1">
        <InputText name="username" type="text" placeholder="Username" fluid />
        <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{ $form.username.error.message }}</Message>
    </div>
    <div class="flex flex-col gap-1">
        <InputText name="firstName" type="text" placeholder="First Name" fluid :formControl="{ validateOnValueUpdate: true }" />
        <Message v-if="$form.firstName?.invalid" severity="error" size="small" variant="simple">{{ $form.firstName.error.message }}</Message>
    </div>
    <div class="flex flex-col gap-1">
        <InputText name="lastName" type="text" placeholder="Last Name" fluid />
        <Message v-if="$form.lastName?.invalid" severity="error" size="small" variant="simple">{{ $form.lastName.error.message }}</Message>
    </div>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## Built In

Although PrimeVue components have built-in support for the Form API, you may still prefer to utilize the components as wrapped with the FormField. This is a matter of preference, for example in case you are also using FormField for other 3rd party components, your own custom components, and native elements, for consistency it may be an option.

```vue
<Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <FormField v-slot="$field" name="username" initialValue="" class="flex flex-col gap-1">
        <InputText type="text" placeholder="Username" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## FormField

The FormField is a helper component that provides validation and tracking for input elements, offering a more flexible structure to bind PrimeVue, non-PrimeVue components or native HTML elements to Form API. Additionally, with props like validateOn* , initialValue , resolver , and name , behaviors can be controlled directly from this component.

```vue
import { FormField } from '@primevue/forms';
```

## Non Prime Vue

Form API is not strictly tied to PrimeVue components, providing a flexible way to manage validation and state for any native HTML elements, your own custom components or third-party libraries.

```vue
<Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <FormField v-slot="$field" name="username" initialValue="" class="flex flex-col gap-1">
        <input type="text" placeholder="Username" :class="[{ error: $field?.invalid }]" v-bind="$field.props" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" name="password" initialValue="PrimeVue" class="flex flex-col gap-1">
        <input v-model="$field.value" type="password" placeholder="Password" :class="[{ error: $field?.invalid }]" @input="$field.onInput" @blur="$field.onBlur" @change="$field.onChange" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## Resolver

Each FormField can have its own dedicated resolver, allowing you to define custom validation logic for individual fields. This flexibility enables tailored validation rules, ensuring that each form field meets specific criteria.

```vue
<Form :initialValues :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-80">
    <FormField v-slot="$field" name="username" initialValue="" :resolver="zodUserNameResolver" class="flex flex-col gap-1">
        <InputText type="text" placeholder="Username" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" name="firstname" initialValue="" :resolver="yupFirstNameResolver" class="flex flex-col gap-1">
        <InputText type="text" placeholder="First Name" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" name="lastname" initialValue="" :resolver="valibotLastNameResolver" class="flex flex-col gap-1">
        <InputText type="text" placeholder="Last Name" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" name="password" initialValue="" :resolver="customPasswordResolver" class="flex flex-col gap-1">
        <Password type="text" placeholder="Password" :feedback="false" toggleMask fluid />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" name="details" class="flex flex-col gap-1">
        <Textarea placeholder="Details" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## Template

It renders as a HTML div element, but this behavior can be modified using the as and asChild props to render different HTML elements or to pass a custom component, allowing for greater flexibility in form structure.

```vue
<Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <FormField v-slot="$field" as="section" name="username" initialValue="" class="flex flex-col gap-2">
        <InputText type="text" placeholder="Username" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" asChild name="password" initialValue="">
        <section class="flex flex-col gap-2">
            <Password type="text" placeholder="Password" :feedback="false" toggleMask fluid />
            <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
        </section>
    </FormField>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## Styled

Component does not apply any styling.

---

