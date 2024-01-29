import { ref, computed, onMounted } from "vue";
import { defineStore } from "pinia";

export const userStore = defineStore('user' , () => {
    const myName = ref('')

    const setMyName = (name) => {
        myName.value = name;
        console.log(myName.value)
      };
    return{ myName, setMyName }
},{persist:true})