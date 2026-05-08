# Forms

The PrimeVue Forms library provides comprehensive form state management with built-in validation support.

## Accessibility

Screen Reader Form does not require any roles and attributes. Keyboard Support Component does not include any interactive elements.

## Basic

All PrimeVue form components are designed for seamless integration with the forms library. Instead of using the standard v-model , the name property is used to link a state object that tracks values, errors, and actions. The form component provides four key properties for state management.

```vue
<Form v-slot="$form" :initialValues :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <div class="flex flex-col gap-1">
        <InputText name="username" type="text" placeholder="Username" fluid />
        <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{ $form.username.error?.message }}</Message>
    </div>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
```

## Download

Forms add-on is available for download on npm registry.

```vue
